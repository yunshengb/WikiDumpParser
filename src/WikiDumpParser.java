import edu.jhu.nlp.wikipedia.*; // download from https://code.google.com/p/wikixmlj/

public class WikiDumpParser {
	public static void main(String[] args) {
		System.out.println(args[0]);
		WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(args[0]);
		try {

			wxsp.setPageCallback(new PageCallbackHandler() { 
				public void process(WikiPage page) {
					if (!page.isRedirect()) {
						
						System.out.println("***********************************");
						System.out.println("title: " + page.getTitle());
						System.out.println(page.getWikiText());
					}
				}
			});

			wxsp.parse();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}