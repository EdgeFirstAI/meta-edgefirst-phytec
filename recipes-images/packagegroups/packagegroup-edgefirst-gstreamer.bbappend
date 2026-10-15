# PHYTEC's GStreamer payload, added so that enabling
# packagegroup-edgefirst-gstreamer on a PHYTEC board brings a working stack
# rather than EdgeFirst's elements alone. meta-edgefirst supplies the plugin
# set on i.MX 8M Plus from meta-freescale-distro, but on i.MX 95 the platform
# GStreamer comes from meta-ampliphy, which is a vendor fact and so belongs
# here. packagegroup-gstreamer also carries the libcamera-gst bridge element.
RDEPENDS:${PN}:append:mx95-nxp-bsp = " \
    packagegroup-gstreamer \
"
