import edu.jhu.nlp.wikipedia.*; // download from https://code.google.com/p/wikixmlj/
import java.io.BufferedWriter;
import java.io.FileWriter;

import lemurproject.indri.IndexEnvironment;

public class WikiDumpParser {
	private static long count = 1587; // number of Wiki pages
	private static final String TXT_ROOT_PATH = "txt_files/";
	private static final String Indri_ROOT_PATH = "Wiki2015/";
	
	public static void main(String[] args) {
		// split the Wiki dump into individual files
		splitWikiDump(args[0]); // specify the path to the Wiki dump as args[0]
		System.out.println("Have created " + count + " txt files.");
		
		// create the new Wiki database
		parseFilesByIndri();
		System.out.println("You get the new database!");
	}
	
//split the Wiki dump into individual files
	private static void splitWikiDump(String WikiDumppath) {
		System.out.println("start parsing " + WikiDumppath);

		// split the Wiki dump to individual files
		WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(WikiDumppath);
		try {
			wxsp.setPageCallback(new PageCallbackHandler() { 
				public void process(WikiPage page) {
					if (!page.isRedirect()) {
						// write the non-redirected page to a file
						String file = TXT_ROOT_PATH + count + ".txt";
						try {
							FileWriter fileWriter =
									new FileWriter(file);
							// always wrap FileWriter in BufferedWriter
							BufferedWriter bufferedWriter =
									new BufferedWriter(fileWriter);
							bufferedWriter.write(page.getWikiText());
							// always close files
							bufferedWriter.close();
							System.out.println(file + " created!");
							++count; // increment count
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
			wxsp.parse(); // start parsing
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// create the Wiki 2015 Indri repository
	private static void parseFilesByIndri() {
		// let Indri parse
		IndexEnvironment env = new IndexEnvironment();
		try {
			env.setMemory(100*1024*1024*1024); // 100 GB
			env.setStemmer("krovetz"); // set the stemmer
			env.create(Indri_ROOT_PATH); // set the root path
			// add all txt files to the Indri repository
			for (long i = 0; i < count; ++i) {
				System.out.println("Adding file " + TXT_ROOT_PATH + i + ".txt");
				env.addFile(TXT_ROOT_PATH + i + ".txt");
			}
			env.close();
			System.out.println("documentsIndexed: " + env.documentsIndexed());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}