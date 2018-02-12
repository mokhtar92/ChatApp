<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : history.xsl
    Created on : February 10, 2018, 9:42 PM
    Author     : Ahmed
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    
    <xsl:variable name="owner" select="/history/@owner"/>
    <xsl:template match="/*[1]">
        <html>
            <head>
                <title>$owner</title>
                <style>
                    
                    .self .avatar:after {
                    content: "";
                    position: relative;
                    display: inline-block;
                    bottom: 19px;
                    right: 0px;
                    width: 0px;
                    height: 0px;
                    border: 5px solid #fff;
                    border-right-color: transparent;
                    border-top-color: transparent;
                    box-shadow: 0px 2px 0px #D4D4D4;
                    }
                </style>
            </head>
           <body>
                <div class="menu">
                    <div class="back">
                        <img src="http://i.imgur.com/DY6gND0.png" draggable="false" />
                    </div>
                    <div class="name"><xsl:value-of select="message/to"/></div>
                    <div class="logo">Void Chat</div>
                </div>
                <ol class="chat">
                    
                    <xsl:for-each select="message">
                        <xsl:choose>
                            <xsl:when test="from = $owner">
                                <li class="self">
                                    <div class="avatar">
                                        <img src="http://i.imgur.com/HYcn9xO.png" draggable="false" />
                                    </div>
                                    <div class="msg">
                                        <p style="
                                    color:{color};
                                    font-size: {size}px;
                                    font-family: {family};
                                    text-decoration: {decoration};
                                    font-weight: {weight};
                                    font-style: {style};">
                                            <xsl:value-of select="body"/>
                                        </p>
                                        <time>
                                            <xsl:value-of select="date"/>
                                        </time>
                                    </div>
                                </li>
                            </xsl:when>
                            <xsl:otherwise>
                                <li class="other">
                                    <div class="avatar">
                                        <img src="http://i.imgur.com/DY6gND0.png" draggable="false" />
                                    </div>
                                    <div class="msg">
                                        <p style="
                                    color:{color};
                                    font-size: {size}px;
                                    font-family: {family};
                                    text-decoration: {decoration};
                                    font-weight: {weight};
                                    font-style: {style};">
                                            <xsl:value-of select="body"/>
                                        </p>
                                        <time>
                                            <xsl:value-of select="date"/>
                                        </time>
                                    </div>
                                </li>
                            </xsl:otherwise>
                        </xsl:choose>
                        
                       
                        
                    
                    
                    </xsl:for-each>

                    
                        
                   
                </ol>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
