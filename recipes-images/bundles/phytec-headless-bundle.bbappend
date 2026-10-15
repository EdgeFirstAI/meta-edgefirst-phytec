require recipes-images/bundles/edgefirst-rauc-signing.inc

# The eMMC on imx95-phyflex-libra-rdk-2 hangs DAT0 part way through an install
# when RAUC runs the adaptive block-hash-index method, which interleaves reads
# of the target slot with the slot write. The controller then fails to reset and
# only a reboot recovers it. A plain sequential slot write completes normally on
# the same hardware, so the method is dropped for this machine; adaptive stays
# enabled everywhere else, where it saves transferring unchanged blocks.
python () {
    if d.getVar("MACHINE") == "imx95-phyflex-libra-rdk-2":
        d.setVarFlag("RAUC_SLOT_rootfs", "adaptive", "")
}
