require include/rcar-gen2-modules-common.inc

LICENSE = "CLOSED"
DEPENDS = "kernel-module-mmngrbuf"
PN = "mmngrbuf-user-module"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PR = "r0"
S = "${WORKDIR}/mmngrbuf"
SRC_URI = "file://mmngrbuf.tar.bz2"

inherit pkgconfig

do_compile() {
    # Build shared library
    cd ${S}/if
    rm -rf ${S}/if/libmmngrbuf.so*
    make all ARCH=arm
    # Copy shared library into shared folder
    cp -P ${S}/if/libmmngrbuf.so* ${LIBSHARED}
}

do_install() {
    mkdir -p ${D}/usr/local/lib/ ${D}/usr/local/include

    # Copy shared library
    cp -P ${S}/if/libmmngrbuf.so* ${D}/usr/local/lib/

    # Copy shared header files
    cp -f ${BUILDDIR}/include/mmngr_buf_user_public.h ${D}/usr/local/include
    cp -f ${BUILDDIR}/include/mmngr_buf_user_private.h ${D}/usr/local/include
}

PACKAGES = "\
    ${PN} \
    ${PN}-dev \
"

FILES_${PN} = " \
    /usr/local/lib/libmmngrbuf.so.* \
"

FILES_${PN}-dev = " \
    /usr/local/include \
    /usr/local/include/*.h \
    /usr/local/lib \
    /usr/local/lib/libmmngrbuf.so \
"

#RPROVIDES_${PN} += "mmngrbuf-user-module"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INSANE_SKIP_${PN} += "libdir"
INSANE_SKIP_${PN}-dev += "libdir"

do_configure[noexec] = "1"

