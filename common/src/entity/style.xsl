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
            <head>
                <style>
		  
                    html,
                    body {
                    height: 100%;
                    }
                    body {
                    margin: 1;
                    background: -webkit-linear-gradient(45deg, #49a09d, #5f2c82);
                    background: linear-gradient(45deg, #49a09d, #5f2c82);
                    font-family: sans-serif;
                    font-weight: 100;
                    }
                    .container {
                    position: absolute;
                    top: 50%;
                    left: 50%;
                    -webkit-transform: translate(-50%, -50%);
                    transform: translate(-50%, -50%);
                    }
                    table {
                    width: 1300px;
                    border-collapse: collapse;
                    overflow: hidden;
                    box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
                    }
                    th,
                    td {
                    padding: 15px;
                    background-color: rgba(255, 255, 255, 0.2);
                    color: #fff;
                    }
                    th {
                    text-align: left;
                    }
                    thead th {
                    background-color: #55608f;
                    }
                    tbody tr:hover {
                    background-color: rgba(255, 255, 255, 0.3);
                    }
                    tbody td {
                    position: relative;
                    }
                    tbody td:hover:before {
                    content: "";
                    position: absolute;
                    left: 0;
                    right: 0;
                    top: -9999px;
                    bottom: -9999px;
                    background-color: rgba(255, 255, 255, 0.2);
                    z-index: -1;
                    }

                </style>
                
            </head>
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
