package tm.model;

// en desuso
// mejorar implementacion
public class Cinta {

	@SuppressWarnings("unused")
	private StringBuffer tape;

	public Cinta(String inputStr, char blank) {
		this.tape = new StringBuffer(buildTape(inputStr, blank));
	}

	private String buildTape(String str, char blank) {
		StringBuilder s = new StringBuilder("$");
		for (int i = 0; i < 5; i++)
			s.append(blank);
		s.append(str);
		for (int i = 0; i < 30; i++)
			s.append(blank);
		s.append('$');
		return s.toString();
	}
}
