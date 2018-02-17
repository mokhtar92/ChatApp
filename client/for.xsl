<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : my.xsl
    Created on : February 16, 2018, 5:26 PM
    Author     : Ahmed
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="//*">
        <html>
            <head>
                <title>my.xsl</title>
            </head>
            <body>
                <xsl:apply-templates/>
                
            </body>
        </html>
    </xsl:template>
    <xsl:template match="message">
        <p>
            <xsl:apply-templates select="to"/>  
            <xsl:apply-templates select="body"/>
        </p>
    </xsl:template>

    <xsl:template match="to">
        TO: <span style="color:#ff0000">
            <xsl:value-of select="."/>
        </span>
        <br />
    </xsl:template>

    <xsl:template match="body">
        Body:  <span style="color:#00ff00">
            <xsl:value-of select="."/>
        </span>
        <br />
    </xsl:template>


</xsl:stylesheet>
