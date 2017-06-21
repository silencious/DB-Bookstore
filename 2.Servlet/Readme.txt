数据库Web开发第二次迭代
JSP+Servlet+Tomcat

WebContent目录：
 使用jquery-easyui；
 META-INF目录下：
  context.xml记录了连接数据库的信息；
 WEB-INF目录：
  使用Dynamic web module 3.0，不需要web.xml文件；
  包含mysql连接的jar包和json数据格式的jar包；

 默认从主页(index.jsp)跳转到搜索页(search.jsp)；
 用户在搜索页键入关键字搜索，请求BookServlet获得数据并显示；
 未登录用户也可以从搜索页选择书目加入购物车(存入session)，可以选择注册(register.jsp)或登录(login.jsp)；
 注册通过请求UserServlet完成；
 登录通过请求UserSession完成；
 登录后跳转到个人中心(me.jsp)，可以查看当前购物车(请求CartSession)和已购买订单及书目(请求OrderServlet)；
 可以选择购买图书，请求Purchase，购买成功后刷新当前页面；
 可以选择注销(请求UserSession)或回到搜索页面继续浏览；
 由SessionListener监听session过期后将购物车中书目存入数据库，登录时会读取；
 
 管理员默认登录后进入manage.jsp页面，可以管理后台数据；
 管理员可以进入statistics.jsp页面，选择用户名、时间、书目种类等，请求StatServlet查看统计数据；


数据库schema：
users(userid int primary, name varchar(32) unique, password varchar(32), email varchar(32))
books(bookid int primary, title varchar(32), author varchar(32), category varchar(32), price double(11,2), stock int)
orders(orderid primary, userid foreign, bookid foregin, num int, time datetime)
cartitems(userid, bookid, num) unique(userid,bookid)

