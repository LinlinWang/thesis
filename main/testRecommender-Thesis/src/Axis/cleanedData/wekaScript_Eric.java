package Axis.cleanedData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class wekaScript_Eric {

	private final static int lowerbound = 933;
	private final static int higherbound = 1851;
	// private final static String MANUALLY_REMOVED =
	// "-R 3,5,8-11,14,16-20,22-27,30-37,39-47,49-51,54,56,59-62,65,68,70-71,73,77-78,85,90,92-93,95-99,101-103,107-113,115,117,119-120,122-124,126-128,130-131,134,136,138-147,149-175,177-185,188-196,198-199,201,207-212,214-215,218,221-223,226-229,232-234,236-239,241-247,249-251,254-255,257,259-263,265-267,269,271-276,278-282,285-288,290,292,294,296,298,300,302,304,306-309,312-316,318-319,321-322,324-325,327-329,332-335,337-343,348,350,352-354,356,358-359,363-368,370-375,377-392,394-402,404-419,422-428,430,432-436,438-440,442-453,455-462,464-466,469-474,476-489,491-492,494-499,501,503-505,507-510,512-516,518-519,521-532,535,537-544,546-552,554-555,557,559-577,579-592,596-602,604-612,614,616,618-621,623,626-638,640-643,645,647-652,654-658,660,662-668,670-675,677,679,682,684-686,688-689,691,693-694,696-698,700,704,708-710,713-714,716,719-724,728-730,733,743,745,763-769,777-780,792-794,796-797,800,803,805,812-815,818-820,824-826,831-833,837-838,843-844,847-848,851,853-854,856-857,861-864,866-872,874-875,879-882,884,886,888,890-893,900,903,905-908,911,913,920,922,924,927,929-930,932";
	// private final static String MANUALLY_REMOVED2 = "-R 56-57,137";
	private final static String MANUALLY_REMOVED = "";
	private final static String MANUALLY_REMOVED2 = "";

	public static String getIndexString(int i) {
		String ret = "-R ";

		if (i == lowerbound + 1)
			ret += lowerbound;
		else if (i > lowerbound + 1) {
			ret += lowerbound + "-" + (i - 1);
		}

		if (i + 1 <= higherbound) {
			if (!ret.endsWith(" "))
				ret += ",";
			if (i + 1 == higherbound)
				ret += higherbound;
			else
				ret += (i + 1) + "-" + higherbound;
		}
		return ret;
	}

	private static Instances removeAttributes(Instances data,
			String filteroptions) throws Exception {
		Remove remove = new Remove(); // new instance of filter
		// set options
		remove.setOptions(weka.core.Utils.splitOptions(filteroptions));
		// inform filter about dataset **AFTER** setting options
		remove.setInputFormat(data);
		// apply filter
		return Filter.useFilter(data, remove);
	}

	public static void main(String[] args) throws Exception {

		// java -cp
		// /Applications/weka-3-7-12-oracle-jvm.app/Contents/Java/weka.jar
		// -Xmx1024m weka.filters.unsupervised.attribute.Remove -R 934-1851 -i
		// package_result_no_string.arff -o filtered.arff

		// java -cp
		// /Applications/weka-3-7-12-oracle-jvm.app/Contents/Java/weka.jar
		// -Xmx1024m weka.classifiers.trees.RandomForest -I 100 -K 0 -S 1
		// -num-slots 1 -t filtered.arff

		BufferedReader reader = new BufferedReader(new FileReader(
				"./src/Axis/cleanedData/package_result_no_string.arff"));
		Instances data = new Instances(reader);
		reader.close();
		// setting class attribute

		System.out
				.println("Name \t s \t f \t n \t F-Measure (S) \t MCC (S) \t Recall(S) \t Precision(S) \t TP(S) \t FP(S) \t TN(S) \t FN(S)");
		// For each test:
		for (int i = lowerbound; i < higherbound; i++) {
			// Remove all other tests
			String indexString = getIndexString(i);
			// System.out.println(indexString);
			Instances newData = removeAttributes(data, indexString);

			// Clean data?
			// newData = removeAttributes(newData, MANUALLY_REMOVED);
			// newData = removeAttributes(newData, MANUALLY_REMOVED2);

			// Tell weka where the test is that we want to predict outcome for
			newData.setClassIndex(newData.numAttributes() - 1);

			// Let's see some information about this test
			Attribute test = newData.attribute(newData.numAttributes() - 1);
			System.out.print(test.name());
			System.out.print("\t");

			Map<String, Integer> classdistribution = new HashMap<String, Integer>();
			classdistribution.put("s", 0);
			classdistribution.put("f", 0);
			classdistribution.put("n", 0);
			for (int n = 0; n < newData.numInstances(); n++) {
				Instance inst = newData.instance(n);
				String c = inst.stringValue(test);
				classdistribution.put(c, classdistribution.get(c) + 1);
			}

			System.out.print(classdistribution.get("s") + "\t");
			System.out.print(classdistribution.get("f") + "\t");
			System.out.print(classdistribution.get("n") + "\t");

			// Now apply the classifier and evaluate it
			String[] classifieroptions = weka.core.Utils
					.splitOptions("-I 100 -K 0 -S 1 -num-slots 1");
			RandomForest classifier = new RandomForest();
			classifier.setOptions(classifieroptions);
			// classifier.buildClassifier(newData);

			Evaluation eval = new Evaluation(newData);
			eval.crossValidateModel(classifier, newData, 10, new Random(1));

			String accuracy = String.valueOf(eval.correct());
			String summary = eval.toSummaryString();
			String classDetails = eval.toClassDetailsString();

			String mccS = String
					.valueOf(eval.matthewsCorrelationCoefficient(0));
			String mccF = String
					.valueOf(eval.matthewsCorrelationCoefficient(1));
			String fmS = String.valueOf(eval.fMeasure(0));
			String fmF = String.valueOf(eval.fMeasure(1));
			String tpS = String.valueOf(eval.numTruePositives(0));
			String fpS = String.valueOf(eval.numFalsePositives(0));
			String tnS = String.valueOf(eval.numTrueNegatives(0));
			String fnS = String.valueOf(eval.numFalseNegatives(0));
			String recS = String.valueOf(eval.recall(0));
			String preS = String.valueOf(eval.precision(0));

			String confusionMatrix = eval.toMatrixString();

			// Perhaps print out the attribute-class distribution distribution
			// newData.attribute(i).
			System.out.println(fmS + "\t" + mccS + "\t" + recS + "\t" + preS
					+ "\t" + tpS + "\t" + fpS + "\t" + tnS + "\t" + fnS);
		}
	}
}
