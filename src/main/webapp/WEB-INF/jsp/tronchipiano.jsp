<!DOCTYPE html>
<html>
<head></head>
<body>

<%@ page import = "java.util.*" %>
<%@ page import="org.teamids.gestionemappe.model.entity.TroncoEntity" %>

<ul>
    <%
        TreeMap<TroncoEntity, HashMap<String, Float>> map = (TreeMap)request.getAttribute("model");
        out.println("<form action=\"http://localhost:8080/gestionemappe/db/aggiornapesitronco\" method=\"post\">");
        for(Map.Entry<TroncoEntity, HashMap<String, Float>> entry : map.entrySet()) {
            out.println("<li>"+entry.getKey().getId()+ " " + entry.getKey().getArea() +"</li>");
            for(Map.Entry<String, Float> subentry : entry.getValue().entrySet()){

                if(subentry.getValue() != null) {
                    out.println(subentry.getKey() + ":<input name=\""+ subentry.getKey()+ "-" +entry.getKey().getId()+"\" type=\"text\" value=\"" + subentry.getValue() + "\">");
                }else{
                    out.println(subentry.getKey() + ":<input name=\""+ subentry.getKey()+ "-" +entry.getKey().getId()+"\" type=\"text\">");
                }
            }
        }
        out.println("<input value=\"Submit\" type=\"submit\">");
        out.println("</form>");
    %>
</ul>

</body>


</html>
