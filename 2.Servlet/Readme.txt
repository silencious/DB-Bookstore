5130379056 ��κ�

���ݿ�Web�����ڶ��ε���
JSP+Servlet+Tomcat

WebContentĿ¼��
 ʹ��jquery-easyui��
 META-INFĿ¼�£�
  context.xml��¼���������ݿ����Ϣ��
 WEB-INFĿ¼��
  ʹ��Dynamic web module 3.0������Ҫweb.xml�ļ���
  ����mysql���ӵ�jar����json���ݸ�ʽ��jar����

 Ĭ�ϴ���ҳ(index.jsp)��ת������ҳ(search.jsp)��
 �û�������ҳ����ؼ�������������BookServlet������ݲ���ʾ��
 δ��¼�û�Ҳ���Դ�����ҳѡ����Ŀ���빺�ﳵ(����session)������ѡ��ע��(register.jsp)���¼(login.jsp)��
 ע��ͨ������UserServlet��ɣ�
 ��¼ͨ������UserSession��ɣ�
 ��¼����ת����������(me.jsp)�����Բ鿴��ǰ���ﳵ(����CartSession)���ѹ��򶩵�����Ŀ(����OrderServlet)��
 ����ѡ����ͼ�飬����Purchase������ɹ���ˢ�µ�ǰҳ�棻
 ����ѡ��ע��(����UserSession)��ص�����ҳ����������
 ��SessionListener����session���ں󽫹��ﳵ����Ŀ�������ݿ⣬��¼ʱ���ȡ��
 
 ����ԱĬ�ϵ�¼�����manage.jspҳ�棬���Թ����̨���ݣ�
 ����Ա���Խ���statistics.jspҳ�棬ѡ���û�����ʱ�䡢��Ŀ����ȣ�����StatServlet�鿴ͳ�����ݣ�


���ݿ�schema��
users(userid int primary, name varchar(32) unique, password varchar(32), email varchar(32))
books(bookid int primary, title varchar(32), author varchar(32), category varchar(32), price double(11,2), stock int)
orders(orderid primary, userid foreign, bookid foregin, num int, time datetime)
cartitems(userid, bookid, num) unique(userid,bookid)

