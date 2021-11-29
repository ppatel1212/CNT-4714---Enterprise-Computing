<!doctype html>
<%
    String sqlStatement = (String) session.getAttribute("sqlStatement");
    String result = (String) session.getAttribute("result");
    if(result == null){
        result = " ";
    }
    if(sqlStatement == null){
        sqlStatement = "select * from suppliers";
    }
%>

<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB"
        crossorigin="anonymous">
    <title>Project 4</title>

     <script type="text/javascript">
        function resetForm() {
            $("#sqlCMD").html("");
        }
      </script>
      
      <script type="text/javascript">
        function clearRes() {
            $("#resdata").remove();
      }
      </script> 

</head>

<body>


    <div class="container-fluid">
        <row class="row justify-content-center">
            <h1 style="color:rgb(3, 6, 94)"class="text-center col-sm-12 col-md-12 col-lg-12">Welcome to the Fall 2021 Project 4 Enterpise Database System</h1>
            <h3 style="color: rgb(5, 91, 131);" class="text-center col-sm-12 col-md-12 col-lg-12"> A Servlet/JSP-based Multi-tiered Enterprise Application Using a Tomcat Container</div>
                <hr size="8px">
            <div class="text-center col-sm-12 col-md-12 col-lg-12"> You are connected to the Project 4 Database.</div>
            <div class="text-center col-sm-12 col-md-12 col-lg-12"> Please enter any valid SQL query or update statement.</div>
            
        </row>
    </div>
    
    <div class="container">
        <row class="row justify-content-center">
            <form action = "sqlServlet" method = "post" style="margin-top: 15px;" class="text-center">
                <div class="form-group row">
                    <div class=" col-sm-12 col-md-12 col-lg-12">
                        <textarea name="sqlStatement" class="form-control" id="sqlCMD" rows="8" cols="40"><%= sqlStatement %></textarea>
                    </div>
                </div>

                <input style="margin-bottom: 10px;" type="submit" class="btn btn-success" value="Execute Command" />
                <button onClick="resetForm();" style="margin-bottom: 10px;" type="reset" class="btn btn-danger">Reset Form</button>
                <button onClick="clearRes();" style="margin-bottom: 10px;" type="reset" class="btn btn-warning">Clear Result</button>
            </form>
        </row>
    </div>


    <div class="text-center">
        <%= result %>
    </div>


    </div>


  

    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
        crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js" integrity="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T"
        crossorigin="anonymous"></script>
</body>

</html>