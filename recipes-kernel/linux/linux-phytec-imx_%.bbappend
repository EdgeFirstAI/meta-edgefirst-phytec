# eIQ Neutron SDK 3.2.x firmware replies 0xA1 to a mailbox reset, where the
# neutron driver in PHYTEC's 6.18.20-based kernel expects 0x0 and waits until
# its retry budget expires. NXP fixed this in the 6.18.37 BSP by negotiating
# the value at runtime, which keeps 3.1.x firmware working too, so the
# backport is additive.
#
# Scoped to i.MX 95 because drivers/staging/neutron exists only on the SoCs
# that carry the Neutron NPU; the i.MX 8M Plus kernel built from this same
# recipe name has no such directory. Drop this append once PHYTEC rebases
# onto a 6.18.37 or later kernel, which already carries the change.

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:mx95-nxp-bsp = " file://0001-staging-neutron-negotiate-the-mailbox-reset-value.patch"
