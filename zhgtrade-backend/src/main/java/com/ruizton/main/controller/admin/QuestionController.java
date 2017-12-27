package com.ruizton.main.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ruizton.main.Enum.QuestionStatusEnum;
import com.ruizton.main.Enum.QuestionTypeEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fadmin;
import com.ruizton.main.model.Fquestion;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.QuestionService;
import com.ruizton.util.Utils;

@Controller
public class QuestionController extends BaseController {
	@Autowired
	private QuestionService questionService ;
	@Autowired
	private AdminService adminService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/questionList")
	@RequiresPermissions("ssadmin/questionList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/questionList") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		
		StringBuffer filter = new StringBuffer();
		filter.append("where 1 = 1 \n");
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and ftelephone like '%"+keyWord+"%' \n");
			modelAndView.addObject("keywords", keyWord);
		}
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}else{
			filter.append("order by fcreateTime \n");
		}
		
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}else{
			filter.append("desc \n");
		}
		List<Fquestion> list = this.questionService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);

		modelAndView.addObject("questionList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "questionList");
		//总数量
		modelAndView.addObject("totalCount", questionService.countForPage(keyWord, null, null, null, null, null));
		return modelAndView ;
	}
	
	@RequestMapping("/ssadmin/questionForAnswerList")
	@RequiresPermissions("ssadmin/questionForAnswerList.html")
	public ModelAndView questionForAnswerList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/questionForAnswerList") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		
		StringBuffer filter = new StringBuffer();
		filter.append("where fstatus =1 \n");
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and fname like '%"+keyWord+"%' \n");
			modelAndView.addObject("keywords", keyWord);
		}
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}else{
			filter.append("order by fcreateTime \n");
		}
		
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}else{
			filter.append("desc \n");
		}
		List<Fquestion> list = this.questionService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		
		modelAndView.addObject("questionList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "questionList");
		modelAndView.addObject("totalCount", questionService.countForPage(keyWord, null, (short)1, null, null, null));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goQuestionJSP")
	public ModelAndView goQuestionJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fquestion question = this.questionService.findById(fid);
			modelAndView.addObject("fquestion", question);
		}
		Map<Integer,String> map = new HashMap<Integer,String>();
/*		map.put(QuestionTypeEnum.CNY_RECHARGE, QuestionTypeEnum.getEnumString(QuestionTypeEnum.CNY_RECHARGE));
		map.put(QuestionTypeEnum.CNY_WITHDRAW, QuestionTypeEnum.getEnumString(QuestionTypeEnum.CNY_WITHDRAW));
*/		map.put(QuestionTypeEnum.COIN_RECHARGE, QuestionTypeEnum.getEnumString(QuestionTypeEnum.COIN_RECHARGE));
		map.put(QuestionTypeEnum.COIN_WITHDRAW, QuestionTypeEnum.getEnumString(QuestionTypeEnum.COIN_WITHDRAW));
		map.put(QuestionTypeEnum.OTHERS, QuestionTypeEnum.getEnumString(QuestionTypeEnum.OTHERS));
		modelAndView.addObject("typeMap", map);
		return modelAndView;
	}
	
	/**
	 * 回复提问
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("ssadmin/updateQuestion")
	@RequiresPermissions("ssadmin/updateQuestion.html")
	public ModelAndView updateQuestion(HttpServletRequest request) throws Exception{
		int fid = Integer.parseInt(request.getParameter("fid"));
		String fanswer = request.getParameter("fanswer");
        questionService.updateReplyQuestion(fid, fanswer, getAdminSession());
		
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","更新成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/answerQuestion")
	@RequiresPermissions("ssadmin/answerQuestion.html")
	public ModelAndView answerQuestion(HttpServletRequest request) throws Exception{
		int fid = Integer.parseInt(request.getParameter("fid"));
		String fanswer = request.getParameter("fanswer");
        Fquestion question = this.questionService.findById(fid);
        question.setFanswer(fanswer);
        question.setFstatus(QuestionStatusEnum.SOLVED);
        question.setfSolveTime(Utils.getTimestamp());
        Fadmin admin = (Fadmin)request.getSession().getAttribute("login_admin");
        question.setFadmin(admin);
        this.questionService.updateObj(question);
		
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","回复成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
}
