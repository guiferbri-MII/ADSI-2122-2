package lambda.layer.data.access.dto;

import java.util.Map;

import org.mongodb.morphia.annotations.Entity;

@Entity("online")
public class OnlineModelDTO extends ModelDTO {

	public OnlineModelDTO(Map<String, Long> freqs, Map<String, Long> freqIdiomas) {
		super(freqs, freqIdiomas);
	}
	
	public OnlineModelDTO() {
		super();
	}
	

}
