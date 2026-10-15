# meta-edgefirst-phytec

EdgeFirst layer for PHYTEC BSP vendor hardware — the phyBOARD-Pollux and phyBOARD-Libra carriers with phyCORE/phyFLEX i.MX 8M Plus and i.MX 95 FPSC modules, built on PHYTEC's `BSP-Yocto-NXP-i.MX*-PD26.1.y` releases.

It sits alongside [`meta-edgefirst`](https://github.com/EdgeFirstAI/meta-edgefirst), which carries the vendor-neutral EdgeFirst recipes, and is the PHYTEC counterpart to [`meta-edgefirst-ezurio`](https://github.com/EdgeFirstAI/meta-edgefirst-ezurio) and [`meta-edgefirst-torizon`](https://github.com/EdgeFirstAI/meta-edgefirst-torizon).

## Why this layer exists

Everything here depends on something PHYTEC-specific, so none of it belongs in the vendor-neutral layer:

- **RAUC is the PHYTEC update mechanism.** Torizon updates over SOTA/OSTree and Ezurio has its own path, so the whole RAUC signing and publishing apparatus is used only by this vendor. Bundles are published under `https://repo.edgefirst.ai/phytec/`.
- **The device keyring is switched on the `update` override**, which PHYTEC's `ampliphy-vendor-rauc` / `ampliphy-*-rauc` distros add to `DISTROOVERRIDES`. No other vendor's distro defines it.
- **The bundle bbappends target `phytec-headless-bundle` and `phytec-artifacts-bundle`**, recipes that live in `meta-ampliphy`.
- **The DHCP client-identifier drop-in** is applied to the `10-end*.network` units that `meta-ampliphy` ships.

## Contents

| Path | Purpose |
|---|---|
| `recipes-core/rauc/rauc-conf.bbappend` | Installs the EdgeFirst root CA as the RAUC verification keyring, replacing PHYTEC's published development CA. `bbfatal`s rather than falling back. |
| `recipes-images/bundles/edgefirst-rauc-signing.inc` | Selects the signing key, leaf and intermediate for a bundle. Separates what a build *trusts* from what it *signs with*, which is what makes a CA migration possible. |
| `recipes-images/bundles/phytec-{headless,artifacts}-bundle.bbappend` | Applies the above to PHYTEC's two bundle recipes. |
| `recipes-core/systemd/systemd-conf_%.bbappend` | Pins the DHCPv4 client identifier to the MAC, so an A/B update does not move the board's lease. |
| `scripts/make-edgefirst-ca.sh` | Generates the offline root, a per-vendor intermediate and a signing leaf (RSA-4096). |
| `scripts/publish-rauc-bundle.sh` | Uploads a bundle, the root certificate and a `latest.json` pointer to S3 behind CloudFront. |

## Setup

Add the layer after `meta-edgefirst` in `BBLAYERS`, then point the build at a signing CA in `conf/local.conf`:

```
EDGEFIRST_CERT_PATH = "${HOME}/edgefirst-ca"
```

Generate that CA first if it does not exist:

```
scripts/make-edgefirst-ca.sh ~/edgefirst-ca phytec
```

A build with `DISTRO_FEATURES` containing `rauc` fails if `EDGEFIRST_CERT_PATH` is unset or the material is incomplete, so a deployable image can never be signed by the published development CA by accident.

Keep the signing keys outside every Yocto tree — they are unencrypted PKCS#8, because BitBake signs non-interactively — and keep the root private key offline. A build reads the leaf key plus the intermediate and root *certificates*, never the root key; the root is needed only to issue a new intermediate.

## Layer dependencies

`ampliphy`, `rauc`, `meta-edgefirst`. Compatible with the `walnascar` (i.MX 8M Plus) and `wrynose` (i.MX 95) Yocto series, matching PHYTEC's two PD26.1.y release lines.
