<!DOCTYPE html>
<html>
<head></head>
<body>

<%@ page import = "java.util.*" %>
<%@ page import="org.teamids.gestionemappe.model.entity.TroncoEntity" %>

<ul>
    <%
        HashMap<TroncoEntity, HashMap<String, Float>> map = (HashMap)request.getAttribute("model");
        for(Map.Entry<TroncoEntity, HashMap<String, Float>> entry : map.entrySet()) {
            out.println("<li>"+entry.getKey().getId()+ " " + entry.getKey().getArea() +"</li>");
            out.println("<form>");
            for(Map.Entry<String, Float> subentry : entry.getValue().entrySet()){

                if(subentry.getValue() != null) {
                    out.println(subentry.getKey() + ":<input type=\"text\" value=\"" + subentry.getValue() + "\">");
                }else{
                    out.println(subentry.getKey() + ":<input type=\"text\">");
                }
            }
            out.println("<input value=\"Submit\" type=\"submit\">");
            out.println("</form>");
        }

    %>
</ul>

</body>


</html>
