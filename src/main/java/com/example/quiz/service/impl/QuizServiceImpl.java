package com.example.quiz.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.quiz.constants.OptionType;
import com.example.quiz.constants.ResMessage;
import com.example.quiz.entity.Quiz;
import com.example.quiz.repository.QuizDao;
import com.example.quiz.service.ifs.QuizService;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.CreateOrUpdateReq;
import com.example.quiz.vo.DeleteReq;
import com.example.quiz.vo.Question;
import com.example.quiz.vo.SearchReq;
import com.example.quiz.vo.SearchRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class QuizServiceImpl implements QuizService{
	@Autowired
	private QuizDao quizDao;
	
	@Override
	public BasicRes createOrUpdate(CreateOrUpdateReq req) {
		//呼叫檢查參數的方法
		BasicRes checkResult = checkParams(req);
		if(checkResult != null) { //表參數有錯誤
			return checkResult; //回傳失敗訊息
		}
//		Quiz中的questions是String , 要將req中的List<question>轉成String
//		透過ObjectMapper將物件轉換成Json格式的字串
		ObjectMapper mapper = new ObjectMapper();
		try {
			String questionStr = mapper.writeValueAsString(req.getQuestionList());
			//若req中的id > 0 表示更新資料， id為空表示新增資料
			//若req是有id的(>0)就透過existsById檢查id是否存在
			if(req.getId() > 0 && !quizDao.existsById(req.getId())){
				//資料不存在 回傳錯誤訊息
					return new BasicRes(ResMessage.UPDATE_ID_NOT_FOUND.getCode(),
							ResMessage.UPDATE_ID_NOT_FOUND.getMessage());
			}
			quizDao.save(new Quiz(req.getId(), req.getName(),req.getDescription(),
					req.getStartDate(),req.getEndDate(),questionStr,req.isPublished()));
		} catch (JsonProcessingException e) {
			return new BasicRes(ResMessage.JSON_PROCESSING_EXCEPTION.getCode(),
					ResMessage.JSON_PROCESSING_EXCEPTION.getMessage());
		}
		
		return new BasicRes(ResMessage.SUCCESS.getCode(),
				ResMessage.SUCCESS.getMessage());
	}
	
	private BasicRes checkParams(CreateOrUpdateReq req) { 
		//檢查問卷參數
		if(!StringUtils.hasText(req.getName())) {
			return new BasicRes(ResMessage.PARAM_QUIZ_NAME_ERROR.getCode(),
					ResMessage.PARAM_QUIZ_NAME_ERROR.getMessage());
		}
		if(!StringUtils.hasText(req.getDescription())) {
			return new BasicRes(ResMessage.PARAM_DESCRIPTION_ERROR.getCode(),
					ResMessage.PARAM_DESCRIPTION_ERROR.getMessage());
		}
		
		//LocalDate.now 是指系統當前時間
		//開始日期早於今日(含)之前 = true, 回傳錯誤訊息
		if(req.getStartDate() == null || !req.getStartDate().isAfter(LocalDate.now())) {
			return new BasicRes(ResMessage.PARAM_START_DATE_ERROR.getCode(),
					ResMessage.PARAM_START_DATE_ERROR.getMessage());
		}
		//結束日期早於開始日期 = true, 回傳錯誤訊息
		if(req.getEndDate() == null || req.getEndDate().isBefore(req.getStartDate())) {
			return new BasicRes(ResMessage.PARAM_END_DATE_ERROR.getCode(),
					ResMessage.PARAM_END_DATE_ERROR.getMessage());
		}
		//檢查題目參數
			//檢查題目list是否有回傳值
		if(CollectionUtils.isEmpty(req.getQuestionList())) {
			return new BasicRes(ResMessage.PARAM_QUESTION_LIST_NOT_FOUND.getCode(),
					ResMessage.PARAM_QUESTION_LIST_NOT_FOUND.getMessage());
		}
		//逐一檢查每一個題目的參數 用foreach()
		for(Question item : req.getQuestionList()) {
			if(item.getId() < 0) {
			return new BasicRes(ResMessage.PARAM_QUESTION_ID_ERROR.getCode(),
					ResMessage.PARAM_QUESTION_ID_ERROR.getMessage());
			}
			if(!StringUtils.hasText(item.getTitle())) {
				return new BasicRes(ResMessage.PARAM_TITLE_ERROR.getCode(),
						ResMessage.PARAM_TITLE_ERROR.getMessage());
			}
			if(!StringUtils.hasText(item.getType())) {
			return new BasicRes(ResMessage.PARAM_TYPE_ERROR.getCode(),
					ResMessage.PARAM_TYPE_ERROR.getMessage());
			}
			//檢查當question type為單選或多選時 options不能為空
			//if ((條件1 || 條件2) && 條件三)
			if((item.getType().equals(OptionType.SINGLE_CHOICE.getType())//
					||	item.getType().equals(OptionType.MULTI_CHOICE.getType()) //
					&& !StringUtils.hasText(item.getOptions())) ) {
				if(!StringUtils.hasText(item.getOptions())) {
					return new BasicRes(ResMessage.PARAM_OPTIONS_ERROR.getCode(),
							ResMessage.PARAM_OPTIONS_ERROR.getMessage());
				}
			}
		}
		
		return null;
		
	}
	
	@Override
	public SearchRes search(SearchReq req) {
		String name = req.getName();
		LocalDate start = req.getStartDate();
		LocalDate end = req.getEndDate();
		int id = req.getQuizId();
		List<Quiz> op = quizDao.findQuizById(id);
		if(!op.isEmpty()) {
			return new SearchRes(ResMessage.SUCCESS.getCode(),
					ResMessage.SUCCESS.getMessage(), op);
		}
		//先假設name為空字串
		//JPA的Containing方法在空字串時會搜尋全部
		if(!StringUtils.hasText(name)){
			name="";
		}
		if( start == null) {
			start = LocalDate.of(1970, 1, 1);
		}
		if( end == null) {
			end = LocalDate.of(2999, 12, 31);
		}
		return new SearchRes(ResMessage.SUCCESS.getCode(),
				ResMessage.SUCCESS.getMessage(), quizDao.
				findByNameContainingAndStartDateAfterAndEndDateBefore(name, start, end));
	}

	@Override
	public BasicRes delete(DeleteReq req) {
		//檢查參數
		if(!CollectionUtils.isEmpty(req.getIdList())) {
			try {
				quizDao.deleteAllById(req.getIdList());
			} catch (Exception e) {
				//當deleteAllById 抓到不存在的值時會報錯 
				//但因為沒有成功刪除東西所以不需要對exception處理
			}
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(),
				ResMessage.SUCCESS.getMessage());
	}
}
