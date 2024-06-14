package com.example.quiz.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.quiz.constants.OptionType;
import com.example.quiz.constants.ResMessage;
import com.example.quiz.entity.Quiz;
import com.example.quiz.entity.Response;
import com.example.quiz.repository.QuizDao;
import com.example.quiz.repository.ResponseDao;
import com.example.quiz.service.ifs.FillinService;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.Feedback;
import com.example.quiz.vo.FeedbackDetail;
import com.example.quiz.vo.FeedbackReq;
import com.example.quiz.vo.FeedbackRes;
import com.example.quiz.vo.Fillin;
import com.example.quiz.vo.FillinReq;
import com.example.quiz.vo.Question;
import com.example.quiz.vo.StatisticsRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FillinServiceImpl implements FillinService {
	// 因為底下的方法都有用到 所以先new出來
	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private ResponseDao responseDao;

	@Autowired
	private QuizDao quizDao;

	//填寫問卷
	@Override
	public BasicRes fillin(FillinReq req) {
		// 1.參數檢查
		BasicRes checkResult = checkParams(req);
		if (checkResult != null) {
			return checkResult;
		}
		// 2.檢查同一個電話號碼是否有重複填同一張問卷
		if (responseDao.existsByQuizIdAndPhone(req.getQuizId(), req.getPhone())) {
			return new BasicRes(ResMessage.DUPLICATE_FILLIN.getCode(), //
					ResMessage.DUPLICATE_FILLIN.getMessage());
		}
		// 3.透過findById檢查quiz_id是否存在 後續用該問卷元素與回應進行比對
		Optional<Quiz> op = quizDao.findById(req.getQuizId());
		if (op.isEmpty()) {
			return new BasicRes(ResMessage.QUIZ_NOT_FOUND.getCode(), //
					ResMessage.QUIZ_NOT_FOUND.getMessage());
		}
		Quiz quiz = op.get(); // 取該id的問卷
		String questionStr = quiz.getQuestions(); // 從Quiz問卷中取出字串questions
		// 預設 fillinStr = null，後續執行 fillinStr =
		// mapper.writeValueAsString(req.getqIdAnswerMap());
		// 把執行得到的結果塞回給 fillinStr 時，會報錯，所以要設成空字串
		String fillInStr = "";
		try {
			// 用ObjectMapper中的readValue將字串轉成Question類別的List
			List<Question> quList = mapper.readValue(questionStr, new TypeReference<>() {
			});
			List<Fillin> finalFillinList = new ArrayList<>(); // 作為最後通過檢查的回答List
			List<Integer> qIdList = new ArrayList<>(); // 紀錄已加進finalFillinList的題號

			
			// 4.比對每一個題目 (從DB中取資料 非從req傳回的資料)
			for (Question item : quList) { // (題號){1,2,3....}
				List<Fillin> fillinList = req.getFillinList();
				for (Fillin fillin : fillinList) {
					// id 不一致，跳過
					if (item.getId() != fillin.getqId()) {
						continue;
					}
					// 若id已存過 跳過
					if (qIdList.contains(item.getId())) {
						continue;
					}
					
					//抽方法檢查選項與答案
					checkResult = checkOptionAnswer(item,fillin);
					if(checkResult != null) {
						return checkResult;
					}
					qIdList.add(fillin.getqId());
					//判斷該題如果必填 但沒有加進qIdList 代表沒有回答 返回錯誤訊息
					if(item.isRequired() && !qIdList.contains(item.getId())) {
						return new BasicRes(ResMessage.ANSWER_IS_REQUIRED.getCode(),
								ResMessage.ANSWER_IS_REQUIRED.getMessage());
					}
					// 新增該題的所有內容到finalFillinList
					// 因為fillin只有qId和answer被檢查過 其餘有可能是不合規定的
					//只有回答(answer)是從fillin取得 其他資料皆從DB拿
					finalFillinList.add(new Fillin(item.getId(), item.getTitle(), item.getOptions(), fillin.getAnswer(), //
							item.getType(), item.isRequired()));
					
				}			
			}
			// 用mapper把FillinList轉成與Response的fillin相同的String類 變數要在try外面宣告
			fillInStr = mapper.writeValueAsString(finalFillinList);
		} catch (Exception e) {
			return new BasicRes(ResMessage.JSON_PROCESSING_EXCEPTION.getCode(),
					ResMessage.JSON_PROCESSING_EXCEPTION.getMessage());
		}
		responseDao.save(new Response(req.getQuizId(), req.getName(), req.getPhone(), //
				req.getEmail(), req.getAge(), fillInStr));
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	//檢查參數
	private BasicRes checkParams(FillinReq req) {
		// 問卷編號錯誤
		if (req.getQuizId() <= 0) {
			return new BasicRes(ResMessage.PARAM_QUIZ_ID_ERROR.getCode(), //
					ResMessage.PARAM_QUIZ_ID_ERROR.getMessage());
		}

		if (!StringUtils.hasText(req.getName())) {
			return new BasicRes(ResMessage.PARAM_NAME_IS_REQUIRED.getCode(), //
					ResMessage.PARAM_NAME_IS_REQUIRED.getMessage());
		}
		if (!StringUtils.hasText(req.getPhone())) {
			return new BasicRes(ResMessage.PARAM_PHONE_IS_REQUIRED.getCode(), //
					ResMessage.PARAM_PHONE_IS_REQUIRED.getMessage());
		}
		if (!StringUtils.hasText(req.getEmail())) {
			return new BasicRes(ResMessage.PARAM_EMAIL_IS_REQUIRED.getCode(), //
					ResMessage.PARAM_EMAIL_IS_REQUIRED.getMessage());
		}
		if (req.getAge() < 12 || req.getAge() > 120) {
			return new BasicRes(ResMessage.PARAM_EMAIL_IS_REQUIRED.getCode(), //
					ResMessage.PARAM_EMAIL_IS_REQUIRED.getMessage());
		}
		return null;
	}
	
	//檢查選項與答案
	private BasicRes checkOptionAnswer(Question item, Fillin fillin) {
		// 1.若該題為必填 且 該key(qId)沒有對應的value(answer) 則返回錯誤訊息
		// 根據題號取得對應的回答
		String answerStr = fillin.getAnswer();
		if (item.isRequired() && !StringUtils.hasText(answerStr)) { // 比對必填題中是否有答案
			return new BasicRes(ResMessage.ANSWER_IS_REQUIRED.getCode(),
					ResMessage.ANSWER_IS_REQUIRED.getMessage());
		}

		// 以分號為條件將答案字串split成陣列
		String[] answerArray = answerStr.split(";");
		// 2.單選題中不能有多個選項
		if (item.getType().equalsIgnoreCase(OptionType.SINGLE_CHOICE.getType()) // 題目為單選題
				&& answerArray.length > 1) { // 答案個數大於1
			return new BasicRes(ResMessage.ANSWER_OPTION_TYPE_IS_NOT_MATCH.getCode(),
					ResMessage.ANSWER_OPTION_TYPE_IS_NOT_MATCH.getMessage());
		}

		// 3.排除題目類型Text
		if (item.getType().equalsIgnoreCase(OptionType.TEXT.getType())) {
			return null;
		}
		// 把 options 切成 Array
		String[] optionArray = item.getOptions().split(";");
		// 要使用List的contains方法 要將optionArray轉成List
		List<String> optionList = List.of(optionArray);
		// 遍歷題目的選項
		for (String str : answerArray) {
			// 假設item,getOptions()的值是: "AB;BC;C;D"
			// 轉成List後 optionList = ["AB","BC","C","D"]
			// 假設answerArray = [AB,B]
			// 用for迴圈比對 optionList 和answerArray 答案會是true, false
			// 4. 必填 && 答案選項不一致
			if (item.isRequired() && !optionList.contains(str)) {
				return new BasicRes(ResMessage.ANSWER_OPTION_IS_NOT_MATCH.getCode(),
						ResMessage.ANSWER_OPTION_IS_NOT_MATCH.getMessage());
			}
			// 5. 非必填 && 有答案 && 答案選項不一致
			if (!item.isRequired() && StringUtils.hasText(str) && !optionList.contains(str)) {
				return new BasicRes(ResMessage.ANSWER_OPTION_IS_NOT_MATCH.getCode(),
						ResMessage.ANSWER_OPTION_IS_NOT_MATCH.getMessage());
			}
		}
		return null;
	}
	
	//查看回覆
	@Override
	public FeedbackRes feedback(FeedbackReq req) {
		// 檢查是否有該問卷
		Optional<Quiz> op = quizDao.findById(req.getQuizId());
		if (op.isEmpty()) {
			return new FeedbackRes(ResMessage.QUIZ_NOT_FOUND.getCode(), //
					ResMessage.QUIZ_NOT_FOUND.getMessage());
		}
		// 取出該題問卷
		Quiz quiz = op.get();
		List<Feedback> feedbackList = new ArrayList<>();
		try {
			// 取得回答的List
			List<Response> resList = responseDao.findByQuizId(req.getQuizId());

			// 遍歷
			for (Response resItem : resList) {
				List<Fillin> fillinList = mapper.readValue(resItem.getFillin(), new TypeReference<>() {});

				FeedbackDetail details = new FeedbackDetail(quiz.getStartDate(), quiz.getEndDate(), //
						quiz.getName(), quiz.getDescription(), resItem.getName(), resItem.getPhone(), //
						resItem.getEmail(), resItem.getAge(), fillinList);

				Feedback feedback = new Feedback(resItem.getName(), resItem.getFillinDateTime(), details);
				feedbackList.add(feedback);
			}
		} catch (Exception e) {
			return new FeedbackRes(ResMessage.JSON_PROCESSING_EXCEPTION.getCode(),
					ResMessage.JSON_PROCESSING_EXCEPTION.getMessage());
		}
		return new FeedbackRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), feedbackList);
	}

	
	//統計一張問卷裡單選和多選的回答數據
	@Override
	public StatisticsRes statistics(FeedbackReq req) {
		List<Response> responseList = responseDao.findByQuizId(req.getQuizId());
		//Map<題號 ,  Map<選項,   被選次數>> 計算的是一張問卷所有回答
		Map<Integer,Map<String,Integer>> countMap = new HashMap<>();
		
		//遍歷取出response的fillin字串
		for(Response item : responseList) {
			String fillinStr = item.getFillin();
			try {
				//將fillin字串轉成List
				List<Fillin> fillinList = mapper.readValue(fillinStr, new TypeReference<>() {});
				//遍歷fillin字串取出選項字串和答案字串
				for(Fillin fillin : fillinList) {
					Map<String,Integer> optionCountMap = new HashMap<>();
					//文字題不列入統計
					if(fillin.getType().equalsIgnoreCase(OptionType.TEXT.getType())) {
						continue;
					}
					String optionStr = fillin.getOptions();
					String[] optionArr = optionStr.split(";");//切分出每個選項
					String answer = fillin.getAnswer();
					//要幫選項和答案的前後加上分號 以確保用來比對的每個選項是唯一
					answer = ";" + answer + ";";
					for(String option : optionArr) { //遍歷每個選項	
						String newOption = ";" + option + ";";
						String newAnswerStr = answer.replace(newOption, "");
						//計算被取代掉的長度後除以選項長度 可得到選項出現的次數
						int count = ((answer.length() - newAnswerStr.length())/ newOption.length());
						optionCountMap = countMap.getOrDefault(fillin.getqId(), optionCountMap);
						//記錄每一題的統計
						int oldCount = optionCountMap.getOrDefault(option, 0);//沒有對應到該key時會返回0
						
						optionCountMap.put(option, oldCount + count);
						countMap.put(fillin.getqId(), optionCountMap);
					}
				}
			} catch (JsonProcessingException e) {
				return new StatisticsRes(ResMessage.JSON_PROCESSING_EXCEPTION.getCode(),
						ResMessage.JSON_PROCESSING_EXCEPTION.getMessage());
			}
			
		}
		Optional<Quiz> op = quizDao.findById(req.getQuizId());
		if(op.isEmpty()) {
			return new StatisticsRes(ResMessage.QUIZ_NOT_FOUND.getCode(),
					ResMessage.QUIZ_NOT_FOUND.getMessage());
		}	
		Quiz quiz = op.get();
		return new StatisticsRes(ResMessage.SUCCESS.getCode(),ResMessage.SUCCESS.getMessage(),//
				quiz.getName(), quiz.getStartDate(), quiz.getEndDate(), countMap);
	
	}



}
