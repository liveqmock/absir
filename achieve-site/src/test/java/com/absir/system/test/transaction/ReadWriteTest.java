/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-25 下午5:45:15
 */
package com.absir.system.test.transaction;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

import com.absir.appserv.client.bean.JQuestion;
import com.absir.appserv.client.bean.JQuestionCategory;
import com.absir.appserv.configure.xls.XlsBase;
import com.absir.appserv.configure.xls.XlsUtils;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.dao.BeanDao;
import com.absir.appserv.system.dao.utils.QueryDaoUtils;
import com.absir.appserv.system.service.BeanService;
import com.absir.appserv.system.service.utils.CrudServiceUtils;
import com.absir.core.helper.HelperFile;
import com.absir.orm.transaction.TransactionUtils;
import com.absir.system.test.AbstractTestInject;
import com.absir.system.test.configure.TestXlsExport;

/**
 * @author absir
 * 
 */
public class ReadWriteTest extends AbstractTestInject {

	/**
	 * @author absir
	 * 
	 */
	public static class QuestionXls extends XlsBase {

		@JaLang("分类名称")
		public String categoryName;

		@JaLang("标题")
		public String title;

		@JaLang("答案一")
		public String choiceA;

		@JaLang("答案二")
		public String choiceB;

		@JaLang("答案三")
		public String choiceC;

		@JaLang("答案四")
		public String choiceD;

		@JaLang("正确答案")
		public int correct;

		@JaLang("最小等级")
		public int minLevel;

		@JaLang("最大等级")
		public int maxLevel;

		public void setQuestion(JQuestion question) {
			categoryName = question.getCategory().getName();
			correct = question.getCorrect();
			maxLevel = question.getMaxLevel();
			minLevel = question.getMinLevel();
			title = question.getTitle();
			choiceA = question.getChoiceA();
			choiceB = question.getChoiceB();
			choiceC = question.getChoiceC();
			choiceD = question.getChoiceD();
		}

		public void insert() {
			JQuestionCategory category = (JQuestionCategory) QueryDaoUtils.select(BeanDao.getSession(), "JQuestionCategory", new Object[] { "o.name", categoryName });
			if (category == null) {
				category = new JQuestionCategory();
				category.setName(categoryName);
				CrudServiceUtils.merge("JQuestionCategory", category, true, null, null);
			}

			JQuestion question = new JQuestion();
			question.setCategory(category);
			question.setTitle(title);
			question.setCorrect(correct);
			question.setMinLevel(minLevel);
			question.setMaxLevel(maxLevel);
			question.setChoiceA(choiceA);
			question.setChoiceB(choiceB);
			question.setChoiceC(choiceC);
			question.setChoiceD(choiceD);
			question.setDifficult(1.0f);
			CrudServiceUtils.merge("JQuestion", question, true, null, null);
		}
	}

	@SuppressWarnings("unchecked")
	public void export() {
		List<JQuestion> questions = (List<JQuestion>) QueryDaoUtils.selectQuery(BeanDao.getSession(), "JQuestion", null, 0, 0);
		List<QuestionXls> questionXlses = new ArrayList<QuestionXls>();
		for (JQuestion question : questions) {
			QuestionXls questionXls = new QuestionXls();
			questionXls.setQuestion(question);
			questionXlses.add(questionXls);
		}

		HSSFWorkbook hssfWorkbook = XlsUtils.getWorkbook(questionXlses);
		try {
			OutputStream outputStream = HelperFile.openOutputStream(new File(TestXlsExport.EXPORT_PATH + QuestionXls.class.getSimpleName() + ".xls"));
			hssfWorkbook.write(outputStream);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void importQuestions() {
		Class<QuestionXls> beanClass = QuestionXls.class;
		try {
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(HelperFile.openInputStream(new File("/Users/absir/Desktop/" + beanClass.getSimpleName() + ".xls")));
			List<QuestionXls> questions = XlsUtils.getXlsList(hssfWorkbook, beanClass);
			for (QuestionXls question : questions) {
				question.insert();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void test() throws InterruptedException {
		TransactionUtils.open(BeanService.TRANSACTION_READ_WRITE, null, null);
		// export();
		// importQuestions();

		export();
		TransactionUtils.close(BeanService.TRANSACTION_READ_WRITE, null, null, null);
	}

}
