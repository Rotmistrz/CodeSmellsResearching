# MSc20FilipMarkiewicz - Code Smells Researching

This software is a research process for Filip Markiewicz's master thesis
about code smells which concerns the comparison of human and automated
code analysing tools perception. This program processes the MLCQ dataset (human perception and
the actual class) and results received from tools PMD (https://pmd.github.io/) and DesigniteJava (https://www.designite-tools.com/designitejava/). The research contains
four code smells: Blob, Data Class, Feature Envy, Long Method.

### Receiving available code snippets of MLCQ dataset.

To reproduce snippets which are reviewed by men being present in MLCQ dataset,
use the ResearchingMLCQProgramReceivingSamples class.
* _filename_ - path to MLCQ reviews file
* _resultsDir_ - directory for results
* _resultsCodeDir_ - directory for code samples
* _resultsReviewsDir_ - directory for _csv_ file containing reviews of reachable samples

### Preparing the comparison between MLCQ dataset and tools

Use ResearchingMLCQProgram class to process the comparison.

* _pathMLCQ_ - path to file with actual MLCQ reviews
* _pathDesigniteJava_ - path to the directory which contains results from DesigniteJava
* _clazz_ - the class representing smell: DesignSmell for Feature Envy and ImplementationSmell for Long Method
* _smellName_ - code smell name of DesigniteJava to be processed (Feature Envy or Long Method)
* _mlcqCodeSmellName_ - name of the smell in MLCQ dataset
* _smellCode_ - standarized code smell code of this research

Then in lines 146-149 put paths to reports received from PMD.
HashMaps number can be other. The results were partitioned
just to get reports in more efficient way.

As you can see, DesigniteJava delivers a bit more complex results structure and
this solution allows to parse one smell in one time (though it could
be improved in the future).

Then, await results in the console.