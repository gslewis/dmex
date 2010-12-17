<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">

<xsl:variable name="show-page-number">true</xsl:variable>

<xsl:variable name="page-height">29.7cm</xsl:variable>
<xsl:variable name="page-width">21cm</xsl:variable>
<xsl:variable name="page-margin-top">1cm</xsl:variable>

<xsl:variable name="page-margin-bottom">
    <xsl:choose>
	<xsl:when test="$show-page-number='true'">2cm</xsl:when>
	<xsl:otherwise>1cm</xsl:otherwise>
    </xsl:choose>
</xsl:variable>

<xsl:variable name="page-margin-left">2.25cm</xsl:variable>
<xsl:variable name="page-margin-right">2.25cm</xsl:variable>
<xsl:variable name="body-margin-top">40pt</xsl:variable>
<xsl:variable name="body-margin-bottom"><xsl:value-of select="$page-margin-bottom"/></xsl:variable>
<xsl:variable name="body-column-count">2</xsl:variable>
<xsl:variable name="body-column-gap">0.5cm</xsl:variable>
<xsl:variable name="before-extent"><xsl:value-of select="$body-margin-top"/></xsl:variable>

<xsl:variable name="after-extent">
    <xsl:choose>
	<xsl:when test="$show-page-number='true'">1.5cm</xsl:when>
	<xsl:otherwise>1cm</xsl:otherwise>
    </xsl:choose>
</xsl:variable>

<xsl:variable name="answer-column-width">2.2cm</xsl:variable>
<xsl:variable name="font-size-normal">12pt</xsl:variable>
<xsl:variable name="font-size-small">10pt</xsl:variable>
<xsl:variable name="font-size-tiny">8pt</xsl:variable>
<xsl:variable name="font-size-large">16pt</xsl:variable>
<xsl:variable name="font-size-title">18pt</xsl:variable>
<xsl:variable name="header-column-left">3cm</xsl:variable>
<xsl:variable name="header-column-right">3cm</xsl:variable>

<xsl:variable name="daisypot-file"></xsl:variable>
<xsl:variable name="daisypot-width"></xsl:variable>
<xsl:variable name="title-text">DaisyMaths</xsl:variable>
<xsl:variable name="sponsor-text">Brought to you by NetSchool</xsl:variable>

<!--
<xsl:variable name="daisypot-file">file:./images/daisypotflower2.gif</xsl:variable>
<xsl:variable name="daisypot-width">2cm</xsl:variable>

<xsl:variable name="title-file">file:./images/title2.gif</xsl:variable>
<xsl:variable name="title-width">9.5cm</xsl:variable>
<xsl:variable name="sponsor-logo-file">file:./images/kraft.gif</xsl:variable>
<xsl:variable name="sponsor-logo-width">2.9cm</xsl:variable>
-->

<xsl:variable name="problem-line-height">24pt</xsl:variable>
<xsl:variable name="problem-space-after">15pt</xsl:variable>
<xsl:variable name="problem-number-width">8mm</xsl:variable>
<xsl:variable name="problem-spacer-width">2mm</xsl:variable>
<xsl:variable name="problem-field-width">10mm</xsl:variable>
<xsl:variable name="spacer">1cm</xsl:variable>

</xsl:stylesheet>
