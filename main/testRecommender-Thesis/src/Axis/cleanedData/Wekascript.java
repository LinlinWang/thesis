package Axis.cleanedData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;

import weka.attributeSelection.AttributeEvaluator;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.RemoveUseless;

public class Wekascript {

	private final static int lowerbound = 933;
	private final static int higherbound = 1851;

	// private final static String MANUALLY_REMOVED =
	// "-R 3,5,8-11,14,16-20,22-27,30-37,39-47,49-51,54,56,59-62,65,68,70-71,73,77-78,85,90,92-93,95-99,101-103,107-113,115,117,119-120,122-124,126-128,130-131,134,136,138-147,149-175,177-185,188-196,198-199,201,207-212,214-215,218,221-223,226-229,232-234,236-239,241-247,249-251,254-255,257,259-263,265-267,269,271-276,278-282,285-288,290,292,294,296,298,300,302,304,306-309,312-316,318-319,321-322,324-325,327-329,332-335,337-343,348,350,352-354,356,358-359,363-368,370-375,377-392,394-402,404-419,422-428,430,432-436,438-440,442-453,455-462,464-466,469-474,476-489,491-492,494-499,501,503-505,507-510,512-516,518-519,521-532,535,537-544,546-552,554-555,557,559-577,579-592,596-602,604-612,614,616,618-621,623,626-638,640-643,645,647-652,654-658,660,662-668,670-675,677,679,682,684-686,688-689,691,693-694,696-698,700,704,708-710,713-714,716,719-724,728-730,733,743,745,763-769,777-780,792-794,796-797,800,803,805,812-815,818-820,824-826,831-833,837-838,843-844,847-848,851,853-854,856-857,861-864,866-872,874-875,879-882,884,886,888,890-893,900,903,905-908,911,913,920,922,924,927,929-930,932";
	// private final static String MANUALLY_REMOVED2 = "-R 56-57,137";
	private final static String MANUALLY_REMOVED = "-R 1-933";
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
		// inform filter about dataset AFTER setting options
		remove.setInputFormat(data);
		// apply filter
		return Filter.useFilter(data, remove);
	}

	public static void main(String[] args) throws Exception {
		
		BufferedReader reader = new BufferedReader(new FileReader(
				"./src/Axis/cleanedData/package_result_no_string.arff"));
		Instances data = new Instances(reader);
		reader.close();
		// setting class attribute

		// System.out.println("Removed\t attributes \t F-Measure (0) \t MCC (0) \t F-Measure (1) \t MCC(1)");
//		System.out
//				.println("Recall(s) \t Recall(f) \t Precision(s) \t Precision(f) \t F-Measure(s) \t F-Measure(f) \t MCC(s) \t MCC(f) \t");
		System.out.println("Recall(s) \t Precision(s) \t F-measure(s) \t ");
		for (int i = lowerbound; i < higherbound; i++) {
			String indexString = getIndexString(i);
			// System.out.println(indexString);

			Instances newData = removeAttributes(data, indexString);

			newData = removeAttributes(newData, MANUALLY_REMOVED);
			newData = removeAttributes(newData, MANUALLY_REMOVED2);
			newData.setClassIndex(newData.numAttributes() - 1);

			String[] classifieroptions = weka.core.Utils
					.splitOptions("-I 100 -K 0 -S 1 -num-slots 1");
			RandomForest classifier = new RandomForest();
			classifier.setOptions(classifieroptions);
			// classifier.buildClassifier(newData);

			Evaluation eval = new Evaluation(newData);
			eval.crossValidateModel(classifier, newData, 10, new Random(1));
			
			DecimalFormat df = new DecimalFormat("#0.00");

			String accuracy = String.valueOf(eval.correct());
			String summary = eval.toSummaryString();
			String classDetails = eval.toClassDetailsString();

			String mcc0 = String //MCC(s)
					.valueOf(df.format(eval.matthewsCorrelationCoefficient(0)));
			String mcc1 = String //MCC(f)
					.valueOf(df.format(eval.matthewsCorrelationCoefficient(1)));
			String fm0 = String.valueOf(df.format(eval.fMeasure(0)));
			String fm1 = String.valueOf(df.format(eval.fMeasure(1)));
			String rc0 = String.valueOf(df.format(eval.recall(0)));
			String rc1 = String.valueOf(df.format(eval.recall(1)));
			String pc0 = String.valueOf(df.format(eval.precision(0)));
			String pc1 = String.valueOf(df.format(eval.precision(1)));
			
			String confusionMatrix = eval.toMatrixString();
			String tps = String.valueOf(df.format(eval.numTruePositives(0)));
			String tpf = String.valueOf(eval.numTruePositives(0));
			String fns = String.valueOf(eval.numFalseNegatives(0));
			String fnf = String.valueOf(eval.numFalseNegatives(1));
			String tns = String.valueOf(eval.numTrueNegatives(0));
			String tnf = String.valueOf(eval.numTrueNegatives(1));

			// Perhaps print out the attribute-class distribution distribution
			// newData.attribute(i).
			System.out.println(indexString + "\t" + rc0 + "\t" + rc1 + "\t"
					+ pc0 + "\t" + pc1 +"\t"+ fm0 + "\t" + fm1 + "\t" + mcc0 + "\t"
					+ mcc1);
//			System.out.println(rc0 + "\t" + rc1 + "\t"
//					+ pc0 + "\t" + pc1 +"\t"+ fm0 + "\t" + fm1 + "\t" + mcc0 + "\t"
//					+ mcc1);
			// System.out.println(confusionMatrix);
		}
	}
}