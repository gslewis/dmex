<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:import href="longmult_html_templates.xsl"/>

<xsl:template match="digit" mode="blank">
    <!-- The number of trailing zeros to reveal. -->
    <xsl:param name="zeros">0</xsl:param>
    <!-- The length of the value to which this digit belongs. -->
    <xsl:param name="length">999</xsl:param>
    <!-- The <input> name suffix ("working[rownum]" or "answer"). -->
    <xsl:param name="suffix"></xsl:param>

    <xsl:choose>
        <xsl:when test="$length - position() &lt; $zeros">
            <td class="blank zero">
                <xsl:value-of select="text()"/>
            </td>
        </xsl:when>
        <xsl:otherwise>
            <xsl:variable name="focus">focus_<xsl:value-of select="@step"/></xsl:variable>

            <!--
            Each input needs a unique name.  The name is of the format:

                longmult_<suffix>[index]

            where <suffix> is "working[rownum]" for working rows or
            "answer" for the answer row and "[index]" is the cell index
            starting from 1.  Remember that working rows have their
            least-significant zeros filled in.
            -->
            <td>
                <xsl:attribute name="class">
                    <xsl:choose>
                        <xsl:when test="@correct='false'">blank error</xsl:when>
                        <xsl:otherwise>blank</xsl:otherwise>
                    </xsl:choose>
                </xsl:attribute>
                <input class="problemFocus" type="text"
                    size="1" maxlength="1">
                    <xsl:attribute name="id"><xsl:value-of select="$focus"/></xsl:attribute>
                    <xsl:attribute name="name">longmult_<xsl:value-of select="$suffix"/>[<xsl:value-of select="position()"/>]</xsl:attribute>
                    <xsl:attribute name="value">
                        <xsl:choose>
                            <xsl:when test="@correct='true'"><xsl:value-of select="text()"/></xsl:when>
                            <xsl:otherwise></xsl:otherwise>
                        </xsl:choose>
                    </xsl:attribute>
                </input>
            </td>
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>

</xsl:stylesheet>
