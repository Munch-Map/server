package skuniv.munchmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MunchmapApplication {


	public static void main(String[] args) {
		SpringApplication.run(MunchmapApplication.class, args);
	}
}


// EC2가 아닌 환경에서 run할 때, EC2 메타데이터를 필요로하는 Configure 제외
//		exclude = {
//				org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration.class, // AWS데이터를 로드하는 자동 구성 클래스, EC2 인스턴스 메타데이터 비활성화
//				org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration.class, // AWS CloudFormation 스택 리소스에 대한 액세스를 위한 자동 구성 클래스. 이 클래스를 제외하면 CloudFormation 스택 리소스를 사용하는 기능을 비활성
//				org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration.class //AWS 리전을 자동으로 감지하는 자동 구성 클래스. 이 클래스를 제외하면 자동 리전 감지 기능을 비활성
//		}