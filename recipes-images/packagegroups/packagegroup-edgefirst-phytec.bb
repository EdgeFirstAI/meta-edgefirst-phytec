SUMMARY = "EdgeFirst Perception Platform - PHYTEC BSP payload"
DESCRIPTION = "Everything an EdgeFirst image carries on a PHYTEC board. \
Composes the vendor-neutral capability packagegroups from meta-edgefirst and \
adds the parts that are PHYTEC facts: meta-ampliphy's own camera packagegroup \
and the phyCAM dewarp configuration. \
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

# No GStreamer. EdgeFirst's components reach the camera through V4L2 and
# libcamera and the video encoder and decoder through the V4L2 memory-to-memory
# API, so the image needs the codec userspace but not a GStreamer stack.
# Anyone wanting one adds packagegroup-edgefirst-gstreamer, which is the single
# switch for it and pulls both EdgeFirst's own elements and the platform
# plugin set. Note that on the walnascar pin edgefirst-gstreamer 0.4.0 does not
# compile against edgefirst-hal 0.28.3 and the recipe carries no LAYERSERIES
# guard, so that switch is i.MX 95 only until it is fixed upstream.
RDEPENDS:${PN} = " \
    packagegroup-edgefirst \
    packagegroup-edgefirst-npu \
    packagegroup-edgefirst-graphics \
    packagegroup-edgefirst-codec \
    packagegroup-edgefirst-studio \
    packagegroup-edgefirst-diagnostics \
"

# i.MX 95 carries the camera stack: libcamera with the NEO ISP IPA from
# meta-edgefirst, PHYTEC's camera tools from meta-ampliphy, and the dewarp
# configuration for the phyCAM modules this board family accepts.
#
# The i.MX 8M Plus line ships no camera stack today.
RDEPENDS:${PN}:append:mx95-nxp-bsp = " \
    packagegroup-edgefirst-camera \
    packagegroup-camera \
    phycam-imx95-dewarp-configs \
"
