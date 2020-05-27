package components;

public class Target {
    
    private String mutationOperator;
    private String className;
    private int mutantNumber;
    private TestCase testCase;
    private String status;
    private boolean isSuspicious;
    private boolean isAchieved;

    public Target(){
        mutationOperator = "";
        testCase = new TestCase();
        isSuspicious = false;
        status = "Alive";
        isAchieved = false;
    }

    public String getMutationOperator() {
        return this.mutationOperator;
    }

    public void setMutationOperator(String mutationOperator) {
        this.mutationOperator = mutationOperator;
    }

    public int getMutantNumber() {
        return this.mutantNumber;
    }

    public void setMutantNumber(int mutantNumber) {
        this.mutantNumber = mutantNumber;
    }

    public TestCase getTestCase() {
        return this.testCase;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isIsSuspicious() {
        return this.isSuspicious;
    }

    public boolean getIsSuspicious() {
        return this.isSuspicious;
    }

    public void setIsSuspicious(boolean isSuspicious) {
        this.isSuspicious = isSuspicious;
    }

    public boolean isIsAchieved() {
        return this.isAchieved;
    }

    public boolean getIsAchieved() {
        return this.isAchieved;
    }

    public void setIsAchieved(boolean isAchieved) {
        this.isAchieved = isAchieved;
    }

    public String getClassName() {
        return this.className;
    }
	public void setClassName(String className) {
        this.className = className;
    }
}