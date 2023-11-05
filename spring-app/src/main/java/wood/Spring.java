package wood;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Spring {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		Bean bean = context.getBean("Bean", Bean.class);
		
		System.out.println(bean.getName());
		
		context.close();
	}
}
