# eIQ Neutron SDK 3.2.x firmware speaks a mailbox protocol the neutron driver
# in PHYTEC's 6.18.20-based kernel does not: it replies 0xA1 to a reset where
# the driver expects 0x0 and waits until its retry budget expires, leaving the
# NPU unusable. NXP reworked the handshake in the 6.18.37 BSP; this is that
# commit, backported verbatim.
#
# The handshake is a version lock, not an additive change: the reset reply
# 0xA1 becomes mandatory, so this kernel requires 3.2.x firmware and will not
# drive 3.1.x. That is safe here because the neutron recipe installs the
# firmware, library and headers from one SDK tree, so a rootfs cannot mix
# generations, and kernel and firmware share a RAUC slot and roll back together.
#
# Scoped to i.MX 95 because drivers/staging/neutron exists only on the SoCs
# that carry the Neutron NPU; the i.MX 8M Plus kernel built from this same
# recipe name has no such directory. Drop this append once PHYTEC rebases
# onto a 6.18.37 or later kernel, which already carries the change.

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:mx95-nxp-bsp = " file://0001-staging-neutron-rework-driver-firmware-mailbox-handshake.patch"
