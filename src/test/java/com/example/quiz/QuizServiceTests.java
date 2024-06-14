package com.example.quiz;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.quiz.constants.OptionType;
import com.example.quiz.service.ifs.QuizService;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.CreateOrUpdateReq;
import com.example.quiz.vo.Question;

@SpringBootTest
public class QuizServiceTests {

	@Autowired
	private QuizService quizService;
	
	@Test
	public void createTest() {
		List<Question> questionList = new ArrayList<>();
		questionList.add(new Question(1,"健康餐?","香煎雞腿;水煮雞胸;牛小排;松阪豬",//
				OptionType.SINGLE_CHOICE.getType(), true));
		
		questionList.add(new Question(1,"丹丹?","1號餐;2號餐;3號餐;4號餐",//
				OptionType.SINGLE_CHOICE.getType(), true));
		questionList.add(new Question(1,"鐵板燒?","肉絲炒飯;海鮮炒飯;黃金蝦仁炒飯;干貝馬鈴薯泥",//
				OptionType.SINGLE_CHOICE.getType(), true));
		CreateOrUpdateReq req = new CreateOrUpdateReq("午餐選擇","午餐吃甚麼",LocalDate.of(2024,05,31),//
				LocalDate.of(2024,05,31), questionList, true);
		BasicRes res = quizService.createOrUpdate(req);
		Assert.isTrue(res.getStatusCode() == 200, "creat test false!" );
		//刪除測試資料 TODO
		//
		
	}
	@Test
	public void updateTest() {
		List<Question> questionList = new ArrayList<>();
		CreateOrUpdateReq req = new CreateOrUpdateReq(8,"午餐選擇","午餐吃甚麼",LocalDate.of(2024,05,31),//
				LocalDate.of(2024,06,30), questionList, true);
		quizService.createOrUpdate(req);
		System.out.println("=====");
	}
	@Test
	public void createErrorTest() {
		List<Question> questionList = new ArrayList<>();
		questionList.add(new Question(1,"健康餐?","香煎雞腿;水煮雞胸;牛小排;松阪豬",//
				OptionType.SINGLE_CHOICE.getType(), true));
		//測試name error
		CreateOrUpdateReq req = new CreateOrUpdateReq("","午餐吃甚麼",LocalDate.of(2024,05,31),//
				LocalDate.of(2024,05,31), questionList, true);
		BasicRes res = quizService.createOrUpdate(req);
		Assert.isTrue(res.getMessage().equalsIgnoreCase("Param name error!"), "creat test1 false!" );
		//測試start date error
		req = new CreateOrUpdateReq("午餐選擇","午餐吃甚麼",LocalDate.of(2024, 5, 3),//
				LocalDate.of(2024,05,31), questionList, true);
		res = quizService.createOrUpdate(req);
		Assert.isTrue(res.getMessage().equalsIgnoreCase("Param start date error!"), "create test2 false!" );
		//測試end date error
		req = new CreateOrUpdateReq("午餐選擇","午餐吃甚麼",LocalDate.of(2024,05,31),//
				LocalDate.of(2024,05,20), questionList, true);
		res = quizService.createOrUpdate(req);
		Assert.isTrue(res.getMessage().equalsIgnoreCase("Param end date error!"), "create test3 false!" );
		//剩餘邏輯都判斷完之後才會去測試成功的場合
		req = new CreateOrUpdateReq("午餐選擇","午餐吃甚麼",LocalDate.of(2024,06,01),//
				LocalDate.of(2024,06,01), questionList, true);
		res = quizService.createOrUpdate(req);
		Assert.isTrue(res.getStatusCode() == 200, "creat test4 false!" );
		
	}
	
}
