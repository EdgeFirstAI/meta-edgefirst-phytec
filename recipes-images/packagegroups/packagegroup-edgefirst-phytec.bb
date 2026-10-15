SUMMARY = "EdgeFirst Perception Platform - PHYTEC BSP payload"
DESCRIPTION = "Everything an EdgeFirst image carries on a PHYTEC board. \
Composes the vendor-neutral capability packagegroups from meta-edgefirst and \
adds the parts that are PHYTEC facts: meta-ampliphy's own GStreamer and \
camera packagegroups, and the phyCAM dewarp configuration. \
\
Installed by the phytec-headless-image bbappend in this layer, so a build is \
reproducible from a manifest sync alone with nothing in local.conf. \
\
Which capabilities a machine gets is decided here rather than inside the \
generic packagegroups: those describe what a capability is, this chooses the \
ones a PHYTEC board ships."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

# packagegroup-edgefirst-gstreamer is deliberately absent. On the walnascar
# pin, edgefirst-gstreamer 0.4.0 does not compile against edgefirst-hal 0.28.3
# - the HAL API moved ahead of it - and the recipe carries no LAYERSERIES
# guard, so there is no walnascar-correct SRCREV to select. Nothing
# hardware-accelerated is lost: NXP's imx-gst1.0-plugin, with meta-edgefirst's
# DMA-BUF zero-copy bbappend, still arrives through
# packagegroup-edgefirst-multimedia, so the G2D and NPU zero-copy paths
# remain. What is missing is EdgeFirst's own elements
# (edgefirstcameraadaptor, edgefirstoverlay). Fixing it belongs upstream in
# meta-edgefirst.
RDEPENDS:${PN} = " \
    packagegroup-edgefirst \
    packagegroup-edgefirst-npu \
    packagegroup-edgefirst-graphics \
    packagegroup-edgefirst-multimedia \
    packagegroup-edgefirst-studio \
    packagegroup-edgefirst-diagnostics \
"

# i.MX 95 carries the camera stack: libcamera with the NEO ISP IPA from
# meta-edgefirst, PHYTEC's GStreamer and camera packagegroups from
# meta-ampliphy, and the dewarp configuration for the phyCAM modules this
# board family accepts.
#
# The i.MX 8M Plus line ships no camera stack today and takes its GStreamer
# from meta-freescale-distro via packagegroup-edgefirst-multimedia instead.
RDEPENDS:${PN}:append:mx95-nxp-bsp = " \
    packagegroup-edgefirst-camera \
    packagegroup-gstreamer \
    packagegroup-camera \
    phycam-imx95-dewarp-configs \
"
