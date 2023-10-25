// Nur Ahmed
// P2: Disaster Relief
// CSE 123 AO
// Brett Wortzman
// 5.17.2023
// This Client class provides methods to simulate disaster relief allocation scenarios.
import java.util.*;
public class Client {
    private static Random rand = new Random();

    public static void main(String[] args) throws Exception {
        // List<Location> scenario = createRandomScenario(10, 10, 100, 1000, 100000);
        List<Location> scenario = createSimpleScenario();
        System.out.println(scenario);
        
        double budget = 2000;
        Allocation allocation = allocateRelief(budget, scenario);
        printResult(allocation, budget);
    }

    // This method allocates the relief funds based on the most people helped by the cost
    // Parameters:
    // - budget: The total budget available for relief allocation.
    // - sites: The list of locations where relief can be allocated to.
    // Returns:
    // - The most optimal allocation of relief funds that maximizes the number of people helped
    //   while minimizing the cost.
    public static Allocation allocateRelief(double budget, List<Location> sites) {
        Set<Allocation> options = generateOptions(budget, sites, 0, new HashSet<>(),
         new HashSet<>());

        int maxPeople = 0;
        double minCost = 0.0;
        Allocation optimalAllocation = new Allocation();

        for (Allocation allocation : options) {
            int peopleHelped = allocation.totalPeople();
            double cost = allocation.totalCost();

            if ((peopleHelped == maxPeople && cost < minCost) || peopleHelped > maxPeople) {
                maxPeople = peopleHelped;
                minCost = cost;
                optimalAllocation = allocation;
            }
        }

        return optimalAllocation;
    }

    // This helper method recursively generates all possible allocation options based on the
    // given budget and list of locations.
    // Parameters:
    // - budget: The total budget available for relief allocation.
    // - sites: The list of locations where relief can be allocated to.
    // - index: The current index in the sites list being considered.
    // - currentAllocation: The set of locations allocated in the current option.
    // - allOptions: The set of all generated allocation options.
    // Returns:
    // - The updated set of all generated allocation options.
    private static Set<Allocation> generateOptions(double budget, List<Location> sites, int index,
     Set<Location> currentAllocation, Set<Allocation> allOptions) {
        if (index >= sites.size()) {
            Allocation allocation = new Allocation();
            for (Location loc : currentAllocation) {
                allocation = allocation.withLoc(loc);
            }
            allOptions.add(allocation);
            return allOptions;
        }
        Location currentSite = sites.get(index);
        Set<Allocation> nonCurrOptions = generateOptions(budget, sites, index + 1,
         currentAllocation, allOptions);
        if (currentSite.getCost() <= budget) {
            Set<Location> updatedAllocation = new HashSet<>(currentAllocation);
            updatedAllocation.add(currentSite);
            Set<Allocation> currOptions = generateOptions(budget - currentSite.getCost(),
            sites, index + 1, updatedAllocation, allOptions);
            allOptions.addAll(currOptions);
        }
        allOptions.addAll(nonCurrOptions);
        return allOptions;
    }

    // PROVIDED HELPER METHODS - **DO NOT MODIFY ANYTHING BELOW THIS LINE!**

    public static void printResult(Allocation alloc, double budget) {
        System.out.println("Result: ");
        System.out.println("  " + alloc);
        System.out.println("  People helped: " + alloc.totalPeople());
        System.out.printf("  Cost: $%.2f\n", alloc.totalCost());
        System.out.printf("  Unused budget: $%.2f\n", (budget - alloc.totalCost()));
    }

    public static List<Location> createRandomScenario(int numLocs, int minPop, int maxPop, double minCostPer, double maxCostPer) {
        List<Location> result = new ArrayList<>();

        for (int i = 0; i < numLocs; i++) {
            int pop = rand.nextInt(minPop, maxPop + 1);
            double cost = rand.nextDouble(minCostPer, maxCostPer) * pop;
            result.add(new Location("Location #" + i, pop, round2(cost)));
        }

        return result;
    }

    public static List<Location> createSimpleScenario() {
        List<Location> result = new ArrayList<>();

        result.add(new Location("Location #1", 50, 500));
        result.add(new Location("Location #2", 100, 700));
        result.add(new Location("Location #3", 60, 1000));
        result.add(new Location("Location #4", 20, 1000));
        result.add(new Location("Location #5", 200, 900));

        return result;
    }    

    private static double round2(double num) {
        return Math.round(num * 100) / 100.0;
    }
}
