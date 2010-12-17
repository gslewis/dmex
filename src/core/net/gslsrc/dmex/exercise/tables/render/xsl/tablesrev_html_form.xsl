<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:import href="tablesrev_html_templates.xsl"/>

<xsl:template name="BlankField">
    <td>
        <xsl:attribute name="class">
            <xsl:choose>
                <xsl:when test="@error='true'">blank error</xsl:when>
                <xsl:otherwise>blank</xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>

        <!-- Largest tablesref answer is 12 x 12 = 144 -->
        <input id="focus_1" class="problemFocus" type="text"
            name="tablesrev_answer" size="3" maxlength="3">
        </input>
    </td>
</xsl:template>

</xsl:stylesheet>
