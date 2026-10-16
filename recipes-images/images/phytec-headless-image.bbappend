# Install the EdgeFirst payload from the image recipe rather than from
# local.conf, so an external user gets the same image from a manifest sync
# alone. An IMAGE_INSTALL:append in a conf file is a datastore operation
# applied after every image recipe's own assignment, so it lands in images it
# was never meant for; here the append is scoped to this recipe by definition.

IMAGE_INSTALL:append = " packagegroup-edgefirst-phytec"
