<%--<!-- 首页 author:yujie 2016-04-20 -->--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ruizton.util.SpringContextUtils" %>
<%@ page import="com.ruizton.main.comm.ConstantMap" %>
<%@include file="includes.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    pageContext.setAttribute("basePath", basePath);

%>
<%
    ConstantMap constantMap = SpringContextUtils.getBean(ConstantMap.class);
    request.setAttribute("constant", constantMap.getMap());
%>
<input type="hidden" id="coinMainUrl" value="<%=basePath%>"/>
<%--<!-- 头部开始 yujie 2016-04-20-->--%>
<div class="header">
    <div class="head" style="background: #f8f8f8;">
        <div class="info">
            <%--<input type="hidden" id="forward_url" value="${sessionScope.forward_url}">--%>
            <div class="phone fl">
                <i class="iconfont" style="width: 20px;display: inline-block">&#xe603;</i> <span>${header_type}
${menu_index}客服热线</span> <span>${requestScope.constant['telephone']}</span>
            </div>

            <div class="fl qq_line" id="qq">
                <i class="fl iconfont" style="line-height: 25px;width: 20px;display: block">&#xe628;</i>
                <a href="http://wpa.qq.com/msgrd?v=3&uin=${requestScope.constant['serviceQQ']}&site=qq&menu=yes" target="_blank">
                    <span class="ml5">在线客服</span>
                </a>
            </div>

            <div class="fl weixin_wrapper pl40" id="weixin">
                <i class="fl iconfont" style="line-height: 30px;width: 20px;display: block">&#xe608;</i>
                <a href="javascript:void(0)"> <span class="ml5">关注微信</span></a>
                <div class="weixin dn">
                    <img alt="众股微信" width="89" height="88" src="${resources}/static/images/index/weixin.jpg" title="众股微信二维码"/>
                </div>
            </div>
            <div class="fl worktime pl40">
                <span>工作时间：9:30 - 18:00</span>
            </div>

            <div class="login_reg fr">
                <%--<!-- 用户登录后 -->--%>
                <div id="userInfoBar" class="logined fl dn">
                    <a id="logoutBtn" class="db fl" href="javascript:void(0);" title="退出">
                        <i class="iconfont db fl" style="margin-top: 3px;width: 20px;display: inline-block">&#xe607;</i><span class="db fl">退出</span>
                    </a>
                    <div id="user_info" class="fl">
                        <div class="user_info clear cp" style="max-width: 275px">
                            <i class="iconfont db fl" style="width: 20px;display: inline-block">&#xe605;</i>
                            <a class="db fl" href="/account/personalinfo.html"><span class="db fl ellipsis" style="max-width: 155px" name="loginName"></span><span class="ml5 db fl" name="userId"></span></a>
                            <a class="messagetip fl c_red dn" href="/account/message.html">未读消息</a>
                            <%--<a class="db f1 " href="/account/message.html">有未读消息</a>--%>
                            <%--<a class="db fl">${login_user}</a>--%>
                            <i class="iconfont db fl" style="width: 20px;display: inline-block">&#xe604;</i>
                        </div>
                        <div class="mywallet_list dn">
                            <div class="clear">
                                <ul class="balance_list fl">
                                    <h4>可用余额</h4>
                                    <li><i class="iconfont db fl c_blue">&#xe61a;</i> <strong class="db fl pl5">人民币：</strong>
                                                <span class="c_green db fl" name="totalRMB"></span>
                                    </li>
                                </ul>
                                <ul class="freeze_list fl">
                                    <h4>委托冻结</h4>
                                    <li><i class="iconfont c_blue db fl">&#xe619;</i> <strong class="db fl pl5">人民币：</strong>
                                                <span class="c_red db fl" name="frozenRMB"></span>
                                    </li>
                                </ul>
                            </div>
                            <div class="mywallet_btn_box">
                                <a href="/account/chargermb.html">充值</a>
                                <a href="/account/withdrawCny.html">提现</a> <a
                                    href="/account/entrusts.html">委托管理</a>
                                <a href="/account/fund.html">个人财务</a>
                            </div>
                            <a href="/account/message.html"> <i class="iconfont c_blue">&#xe614;</i>
                                <span>系统消息</span>
                                <div class="information c_red f16" id="unReadInformation"></div>
                            </a>

                        </div>
                    </div>
                </div>
                <%--<!-- 用户登录前 -->--%>
                <div id="loginBar" class="dn">
                    <div class="login fl">
                        <a class="db fl" title="登录" id="login" href="javascript:void(0)"> <i class="iconfont">
                            &#xe605;</i><span>登录</span>
                        </a>
                    </div>
                    <div class="reg fl">
                        <a href="/user/to_reg.html" class="db fl" title="注册"> <i class="iconfont">&#xe606;</i><span>注册</span>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="flow" id="top_nav">
        <div class="nav" id="nav">
            <div class="logo fl">
                <a class="db" href="/"> <img src="${cdn}${requestScope.constant['logoImage']}" height="55px" alt="数字货币交易网站"
                                             title="众股logo"/>
                </a>
            </div>

        </div>
    </div>
</div>
<%--<!-- 头部结束 -->--%>
<%--<!-- 登录窗口-->--%>
<%@include file="login_box.jsp" %>
<script src="${resources}/static/js/jquery/jquery-1.11.1.min.js"></script>