package Axis.cleanedData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;

//output file contains wrong attributes
public class FormatFile {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		FormatFile f = new FormatFile();
//		 f.CSV2Arff();
//		 f.formatAttributes();
		// f.ConvertInstancePackage();
		// f.ConvertInstanceResult();
//		 f.Arff2CSV();
		 f.mergeInstances();
//		 f.uniqueSymbol();
//		f.Delete();
		// f.Count();
	}

	public void ConvertInstanceResult() throws IOException {
		// Get input file.
		FileInputStream fstream = new FileInputStream("test.txt");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		String[] testName;

		// creat output file.
		File writename = new File("textOutput.txt");
		writename.createNewFile();
		BufferedWriter out = new BufferedWriter(new FileWriter(writename));

		while ((strLine = br.readLine()) != null) {
			testName = strLine.split(",");
			// keep the first value ()
			System.out.println(0 + ":" + testName[0]);
			out.write(testName[0] + ",");
			// set the second value as nc (no change)
			if (testName[1].contains("-")) {
				// out.write(testName[1].replace(testName[1], "ig") +
				// ",");//module->package
				out.write(testName[1].replace(testName[1], "n") + ",");// test->result
				System.out.println(1 + ": "
						+ testName[1].replace(testName[1], "n"));
			} else {
				// out.write(testName[1].replace(testName[1], "nc") + ",");
				out.write(testName[1].replace(testName[1], "s") + ",");
				System.out.println(1 + ": "
						+ testName[1].replace(testName[1], "s"));
			}
			for (int i = 2; i < testName.length; i++) {
				if (testName[i].contains("-")) {
					// out.write(testName[i].replace(testName[i], "ig") + ",");
					out.write(testName[i].replace(testName[i], "n") + ",");
					System.out.println(i + ": "
							+ testName[i].replace(testName[i], "n"));
				} else if (testName[i].equals(testName[i - 1])) {
					// out.write(testName[i].replace(testName[i], "nc") + ",");
					out.write(testName[i].replace(testName[i], "s") + ",");
					System.out.println(i + ": "
							+ testName[i].replace(testName[i], "s"));
				} else if (testName[i].equals(testName[i - 1]) == false) {
					if (testName[i - 1].contains("-")) {
						for (int k = 1; k < i; k++) {
							if (testName[i - 1 - k].contains("-") == false) {
								if (testName[i - 1 - k].equals(testName[i])) {
									// out.write(testName[i].replace(testName[i],
									// "nc") + ",");
									out.write(testName[i].replace(testName[i],
											"s") + ",");
									System.out.println(i
											+ ": "
											+ testName[i].replace(testName[i],
													"s"));
									break;
								} else {
									// out.write(testName[i].replace(testName[i],
									// "c") + ",");
									out.write(testName[i].replace(testName[i],
											"s") + ",");
									System.out.println(i
											+ ": "
											+ testName[i].replace(testName[i],
													"s"));
									break;
								}
							}
						}
					} else {
						// out.write(testName[i].replace(testName[i], "c") +
						// ",");
						out.write(testName[i].replace(testName[i], "f") + ",");
						System.out.println(i + ": "
								+ testName[i].replace(testName[i], "f"));
					}
				} else {
					out.write(testName[i] + ",");
					System.out.println(i + ": " + testName[i]);
				}
			}
			out.write("\n");
			out.flush();
		}
		in.close();
		out.close();

	}

	public void Count() throws IOException {
		// Open the file
		FileInputStream fstream = new FileInputStream("test.txt");
		// Get input file.
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		String[] testName;

		File writename = new File("textOutput.txt");
		writename.createNewFile();
		BufferedWriter out = new BufferedWriter(new FileWriter(writename));

		while ((strLine = br.readLine()) != null) {
			testName = strLine.split(",");
			for (int i = 0; i < testName.length; i++) {
				System.out.println(i + ": " + testName[i] + "\n");
				out.write(i + ": " + testName[i] + "\n");
				out.flush();
			}
		}
		in.close();
		out.close();
	}

	public void Delete() throws IOException {
		// delelte , at the end of each line
		FileInputStream fstream = new FileInputStream("test.txt");
		// Get input file.
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		String testName;
		// creat output file.
		File writename = new File("textOutput.txt");
		writename.createNewFile();
		BufferedWriter out = new BufferedWriter(new FileWriter(writename));

		while ((strLine = br.readLine()) != null) {
			testName = strLine.substring(0, strLine.length() - 1);
			out.write(testName + "\n");
			System.out.println(testName);
			out.flush();
		}
		in.close();
		out.close();
	}

	public void CSV2Arff() throws IOException {
		// The attributes have wrong format in the output arff file.
		// Then call the splitAttributes class to fix the format.

		// load result CSV
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File("./src/Axis/cleanedData/result_converted.csv"));
		Instances data = loader.getDataSet();
		// Save ARFF
		ArffSaver saveResult = new ArffSaver();
		saveResult.setInstances(data);
		saveResult.setFile(new File(
				"./src/Axis/cleanedData/result_converted.arff"));

		// setting class attribute
		data.setClassIndex(data.numAttributes() - 1);
		saveResult.writeBatch();
		System.out.println("result_converted.arff created!");

		// load package CSV
		CSVLoader loadPackage = new CSVLoader();
		loadPackage.setSource(new File(
				"./src/Axis/cleanedData/package_converted.csv"));
		Instances data2 = loadPackage.getDataSet();

		// Save ARFF
		ArffSaver savePackage = new ArffSaver();
		savePackage.setInstances(data2);
		savePackage.setFile(new File(
				"./src/Axis/cleanedData/package_converted.arff"));
		savePackage.writeBatch();
		System.out.println("packages_origenal.arff created!");
	}

	public void formatAttributes() throws IOException {
		// Convert the attribute formate to arff formate.
		FileInputStream fstream = new FileInputStream("test.txt");
		// Get input file.
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		String[] testName;

		// creat output file.
		File writename = new File("textOutput.txt");
		writename.createNewFile();
		BufferedWriter out = new BufferedWriter(new FileWriter(writename));

		while ((strLine = br.readLine()) != null) {
			testName = strLine.split(";");
			for (int i = 0; i < testName.length; i++) {
				System.out.println(i + "@attribute " + testName[i] + "\n");
//				out.write("@attribute " + testName[i] + " {nc,c,ig}" + "\n");// module->package
				 out.write("@attribute " + testName[i] + " {s,f,n}" +
				 "\n");//test->result
				out.flush();
			}
		}
		in.close();
		out.close();
	}

	public void ConvertInstancePackage() throws IOException {
		// Get input file.
		FileInputStream fstream = new FileInputStream("test.txt");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		String[] testName;

		// creat output file.
		File writename = new File("textOutput.txt");
		writename.createNewFile();
		BufferedWriter out = new BufferedWriter(new FileWriter(writename));

		while ((strLine = br.readLine()) != null) {
			testName = strLine.split(",");
			// keep the first value ()
			System.out.println(0 + ":" + testName[0]);
			out.write(testName[0] + ",");
			// set the second value as nc (no change)
			if (testName[1].contains("-")) {
				out.write(testName[1].replace(testName[1], "ig") + ",");// module->package
				System.out.println(1 + ": "
						+ testName[1].replace(testName[1], "ig"));
			} else {
				out.write(testName[1].replace(testName[1], "nc") + ",");
				System.out.println(1 + ": "
						+ testName[1].replace(testName[1], "nc"));
			}
			for (int i = 2; i < testName.length; i++) {
				if (testName[i].contains("-")) {
					out.write(testName[i].replace(testName[i], "ig") + ",");
					System.out.println(i + ": "
							+ testName[i].replace(testName[i], "ig"));
				} else if (testName[i].equals(testName[i - 1])) {
					out.write(testName[i].replace(testName[i], "nc") + ",");
					System.out.println(i + ": "
							+ testName[i].replace(testName[i], "nc"));
				} else if (testName[i].equals(testName[i - 1]) == false) {
					if (testName[i - 1].contains("-")) {
						for (int k = 1; k < i; k++) {
							if (testName[i - 1 - k].contains("-") == false) {
								if (testName[i - 1 - k].equals(testName[i])) {
									out.write(testName[i].replace(testName[i],
											"nc") + ",");

									System.out.println(i
											+ ": "
											+ testName[i].replace(testName[i],
													"nc"));
									break;
								} else {
									out.write(testName[i].replace(testName[i],
											"nc") + ",");
									System.out.println(i
											+ ": "
											+ testName[i].replace(testName[i],
													"nc"));
									break;
								}
							}
						}
					} else {
						out.write(testName[i].replace(testName[i], "c") + ",");
						System.out.println(i + ": "
								+ testName[i].replace(testName[i], "c"));
					}
				} else {
					out.write(testName[i] + ",");
					System.out.println(i + ": " + testName[i]);
				}
			}
			out.write("\n");
			out.flush();
		}
		in.close();
		out.close();

	}

	public void Arff2CSV() throws IOException {
		// The format of output cvs file is wrong. all the value in the same
		// column
		// load ARFF
		ArffLoader loader = new ArffLoader();
		loader.setSource(new File(
				"./src/Axis/cleanedData/date_result.arff"));
		Instances data = loader.getDataSet();

		// save CSV
		CSVSaver saver = new CSVSaver();
		saver.setInstances(data);
		saver.setFile(new File(
				"./src/Axis/cleanedData/date_result.csv"));
		saver.writeBatch();
		System.out.print("csv file created!");
	}

	public void mergeInstances() throws IOException {
		String packages = "test.txt"; // contains instances of packages file
		String results = "test2.txt"; // contains instances of results file
		BufferedReader reBr = new BufferedReader(new FileReader(packages));
		BufferedReader paBr = new BufferedReader(new FileReader(results));

		File writename = new File("textOutput.txt");
		writename.createNewFile();
		BufferedWriter out = new BufferedWriter(new FileWriter(writename));

		int index = 0;
		while (true) {
			index++;
			String partOne = reBr.readLine();
			String partTwo = paBr.readLine();

			if (partOne == null || partTwo == null)
				break;

			System.out.println(partOne + "\t" + partTwo);
			// out.write(partTwo + "," + partOne+"\n"); //result_and_package
			out.write(partOne + "," + partTwo + "\n");// package_and_result
		}
	}

	public void uniqueSymbol() throws IOException {
		// add uniqueSymbol(Test/Module) before each attributes for both package
		// and result
		// files
		String pAttribute = "test.txt"; // attributes of package file ..without
										// @attribute
		BufferedReader reBr = new BufferedReader(new FileReader(pAttribute));
		File addSymbol = new File("testOutput.txt");
		addSymbol.createNewFile();
		BufferedWriter out = new BufferedWriter(new FileWriter(addSymbol));

		int index = 0;
		while (true) {
			index++;
			String partOne = reBr.readLine();
			if (partOne == null)
				break;
//			 System.out.println("@attribute "+("Module"+partOne).replace(" ",
//			 "")+" {nc,c,ig}"+ "\t");
//			 out.write("@attribute "+("Module"+partOne).replace(" ",
//			 "")+" {nc,c,ig}"+ "\n");
			System.out
					.println("@attribute "
							+ ("Test" + partOne).replace(" ", "")
							+ " {s,f,n}" + "\t");
			out.write("@attribute " + ("Test" + partOne).replace(" ", "")
					+ " {s,f,n}" + "\n");
		}
	}
}
