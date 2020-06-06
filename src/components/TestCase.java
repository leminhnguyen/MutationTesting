package components;


public class TestCase {
    
    private String testCase;
    private double stateFitness;
    private int approachLevel;
    private double localFitness;
    private String necessityCost;
    private String sufficiencyCost;
    private String status;
    private boolean hasFitness;
    private double weight;
    private String className;
    private String objectName;
    private String method; 

    public TestCase(){
        testCase = "";
        hasFitness = false;
        className = "";
        objectName = "";
        weight = 0.0;
    }

    public TestCase(TestCase tc) {
        this.testCase = tc.testCase;
        this.stateFitness = tc.stateFitness;
        this.approachLevel = tc.approachLevel;
        this.localFitness = tc.localFitness;
        this.necessityCost = tc.necessityCost;
        this.sufficiencyCost = tc.sufficiencyCost;
        this.status = tc.status;
        this.hasFitness = tc.hasFitness;
        this.weight = tc.weight;
        this.className = tc.className;
        this.objectName = tc.objectName;
        this.method = tc.method;
    }


    public String toString() {
        return testCase;
    }

    public String getTestCase() {
        return this.testCase;
    }

    public void setTestCase(String testCase) {
        this.testCase = testCase;
    }


    public double getStateFitness() {
        return this.stateFitness;
    }

    public void setStateFitness(double stateFitness) {
        this.stateFitness = stateFitness;
    }

    public int getApproachLevel() {
        return this.approachLevel;
    }

    public void setApproachLevel(int approachLevel) {
        this.approachLevel = approachLevel;
    }

    public double getLocalFitness() {
        return this.localFitness;
    }

    public void setLocalFitness(double localFitness) {
        this.localFitness = localFitness;
    }

    public String getNecessityCost() {
        return this.necessityCost;
    }

    public void setNecessityCost(String necessityCost) {
        this.necessityCost = necessityCost;
    }

    public String getSufficiencyCost() {
        return this.sufficiencyCost;
    }

    public void setSufficiencyCost(String sufficientCost) {
        this.sufficiencyCost = sufficientCost;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getFitness() {
        return this.hasFitness;
    }

    public void setFitness(boolean hasFitness) {
        this.hasFitness = hasFitness;
    }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getObjectName() {
        return this.objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}