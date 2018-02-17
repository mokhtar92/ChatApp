<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : style.xsl
    Created on : February 16, 2018, 4:12 PM
    Author     : Ahmed
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
   
   <xsl:template match="/">        
        <html>
            <head> hi</head>
            <body>
                
                <table border="1">
                    <tr bgcolor="#9acd32">
                        <th>From</th>
                        <th>To</th>
                        <th>Body</th>
                        <th>Date</th>
                        <th>Time</th>   
                    </tr>
                    <xsl:for-each select="chat/message">
                        <tr>
                            <td>
                                <xsl:value-of select="from" />
                            </td>
                            <td>
                                <xsl:value-of select="to" />
                            </td>
                            <td>
                                <xsl:value-of select="body" />
                            </td>
                            <td>
                                <xsl:value-of select="date" />
                            </td>
                             <td>
                                <xsl:value-of select="time" />
                            </td>
                        </tr>
                        
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
