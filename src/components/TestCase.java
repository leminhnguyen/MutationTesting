package components;


public class TestCase {
    
    private String testCase;
    private double state_fitness;
    private int approach_level;
    private double local_fitness;
    private String necessity_cost;
    private String sufficient_cost;
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
        this.state_fitness = tc.state_fitness;
        this.approach_level = tc.approach_level;
        this.local_fitness = tc.local_fitness;
        this.necessity_cost = tc.necessity_cost;
        this.sufficient_cost = tc.sufficient_cost;
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
        return this.state_fitness;
    }

    public void setStateFitness(double state_fitness) {
        this.state_fitness = state_fitness;
    }

    public int getApproach_level() {
        return this.approach_level;
    }

    public void setApproach_level(int approach_level) {
        this.approach_level = approach_level;
    }

    public double getLocal_fitness() {
        return this.local_fitness;
    }

    public void setLocal_fitness(double local_fitness) {
        this.local_fitness = local_fitness;
    }

    public String getNecessity_cost() {
        return this.necessity_cost;
    }

    public void setNecessity_cost(String necessity_cost) {
        this.necessity_cost = necessity_cost;
    }

    public String getSufficient_cost() {
        return this.sufficient_cost;
    }

    public void setSufficient_cost(String sufficient_cost) {
        this.sufficient_cost = sufficient_cost;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isHasFitness() {
        return this.hasFitness;
    }

    public boolean getHasFitness() {
        return this.hasFitness;
    }

    public void setHasFitness(boolean hasFitness) {
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