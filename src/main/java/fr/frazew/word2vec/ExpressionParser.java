package fr.frazew.word2vec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionParser {
	private String query;
	
	private Pattern pattern = Pattern.compile("(\\s*?.*?\\s*?(\\+|-)\\s*?.*?\\s?)");
	private Pattern pattern2 = Pattern.compile("(\\+|-)");
	
	public static final String PLUS = "+";
	public static final String MINUS = "-";
	
	public ArrayList<String> terms;
	public ArrayList<String> operations;
	
	public ExpressionParser(String query) {
		this.query = query;
	}
	
	public ExpressionParser parse() {
		Matcher matcher = this.pattern.matcher(this.query);
		List<String> matches = new ArrayList<String>();
		while (matcher.find()) matches.add(matcher.group());
		
		terms = new ArrayList<String>();
		operations = new ArrayList<String>();
		String global = "";
		for (String string : matches) {
			if (string == PLUS || string == MINUS) {
				operations.add(string);
			} else {
				global += string;
				terms.add(cleanString(string));
			}
		}
		
		Matcher matcher2 = this.pattern2.matcher(this.query);
		while (matcher2.find()) operations.add(matcher2.group());
		
		terms.add(this.query.replace(global, "").trim());
		
		return this;
	}
	
	public HashMap<String[], String> getTermsCombo() {
		HashMap<String[], String> result = new HashMap();
		
		for (int i = 0; i < this.terms.size(); i++) {
			if (i+1 < this.terms.size()) {
				String[] couple = new String[2];
				couple[0] = this.terms.get(i);
				couple[1] = this.terms.get(i+1);
				i++;
	
				String type = "";
				type = this.operations.get(i - 1);
				result.put(couple, type);
			} else {
				result.put(new String[] {this.terms.get(i)}, this.operations.get(i - 1));
			}
		}
		
		return result;
	}
	
	private String cleanString(String string) {
		string = string.replaceAll("\\s*?(\\+|-)\\s*?", "");
		return string.trim();
	}
}
