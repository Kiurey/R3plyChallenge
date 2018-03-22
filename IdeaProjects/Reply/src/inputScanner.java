public class inputScanner {


    private int ProvidersQuantity, ServicesQuantity, CountriesNumber, ProjectsQuantity;
    private String[] Country;
    private project[] projects;
    private provider[] Provider;
    private int InputPosition = 0;
    private int TotalProvidersNumber = 0;
    private double AvPenalty = 0;
    private double AvItems = 0;

    public inputScanner(String[] InputString) {
        parseInput(InputString);
        setAv();
    }


    private void parseInput(String[] InputString) {
        ProvidersQuantity = Integer.parseInt(InputString[0]);
        ServicesQuantity = Integer.parseInt(InputString[1]);
        CountriesNumber = Integer.parseInt(InputString[2]);
        ProjectsQuantity = Integer.parseInt(InputString[3]);

        Country = new String[CountriesNumber];
        projects = new project[ProjectsQuantity];

        int CountryCounter = 0;
        InputPosition = 4 + ServicesQuantity;

        while (CountryCounter < CountriesNumber) {
            Country[CountryCounter] = InputString[InputPosition];
            CountryCounter++;
            InputPosition++;
        }

        int PackQuantity;
        float PackCost;
        int[] CountryLatency = new int[CountriesNumber];
        int[] Services = new int[ServicesQuantity];


        //makeProvidersStack();
        stack ProvidersStack = new stack();
        for (int CurrentProviderValue = 0; CurrentProviderValue < ProvidersQuantity; CurrentProviderValue++) {

            InputPosition++;
            int ProviderRegions = Integer.parseInt(InputString[InputPosition]);
            InputPosition++;
            provider CurrentProvider;
            for (int CurrentRegion = 0; CurrentRegion < ProviderRegions; CurrentRegion++) {
                InputPosition++;

                PackQuantity = Integer.parseInt(InputString[InputPosition]);
                InputPosition++;
                PackCost = Float.parseFloat(InputString[InputPosition]);
                InputPosition++;

                for (int CurrentService = 0; CurrentService < ServicesQuantity; CurrentService++){
                    Services[CurrentService] = Integer.parseInt(InputString[InputPosition]);
                    InputPosition++;
                }

                for (int CurrentLatency = 0; CurrentLatency < CountriesNumber; CurrentLatency++) {
                    CountryLatency[CurrentLatency] = Integer.parseInt(InputString[InputPosition]);
                    InputPosition++;
                }

                //make object with everything
                //int PackQuantity, int PackCost, int[] Services, int[] Latency
                CurrentProvider = new provider(PackQuantity, PackCost, Services.clone(),CountryLatency.clone(), CurrentProviderValue, CurrentRegion);
                //add to stack
                ProvidersStack.addToStack(CurrentProvider);
                //end "for" for Region
            }

            //end "for" for Providers
        }


        //Provider array maker
        providerArrayMaker(InputString, projects, Services, ProvidersStack);


    }

    private void providerArrayMaker(String[] s, project[] projects, int[] services, stack providersStack) {
        TotalProvidersNumber = providersStack.getStackLength();
        Provider = new provider[TotalProvidersNumber];
        int ProviderCount = 0;
        while (ProviderCount < TotalProvidersNumber) {
            Provider[ProviderCount] = providersStack.getFromStack();
            ProviderCount++;
        }


        for (int CurrentProject = 0; CurrentProject < ProjectsQuantity; CurrentProject++) {
            int Penalty = Integer.parseInt(s[InputPosition]);
            InputPosition++;

            //search for country number
            int CountryValue = getCountryValue(s[InputPosition]);
            InputPosition++;

            for (int CurrentService = 0; CurrentService < ServicesQuantity; CurrentService++) {
                services[CurrentService] = Integer.parseInt(s[InputPosition]);
                InputPosition++;
            }

            //make class and add it to array
            project ThisProject = new project(Penalty, CountryValue, services, TotalProvidersNumber);
            projects[CurrentProject] = ThisProject;
            //System.out.println(projects[CurrentProject].getPenaltyForItem());
        }
    }

    private int getCountryValue(String s) {
        int CountryValue = 0;
        while (!(Country[CountryValue].equals(s))) {
            CountryValue++;
        }
        return CountryValue;
    }

    private void setAv(){
        double TotalItems = 0;
        double TotalPenalty = 0;
        for(int CurrentProject = 0; CurrentProject < ProjectsQuantity; CurrentProject++){
            TotalItems = TotalItems + projects[CurrentProject].getTotalItems();
            TotalPenalty = TotalPenalty + projects[CurrentProject].getProjectPenalty();
        }
        AvItems = TotalItems / ProjectsQuantity;
        AvPenalty = TotalPenalty / ProjectsQuantity;
    }


    //GETTERS

    public int getServicesQuantity() {
        return ServicesQuantity;
    }

    public int getCountriesNumber() {
        return CountriesNumber;
    }

    public int getProjectsQuantity() {
        return ProjectsQuantity;
    }

    public String[] getCountry() {
        return Country;
    }

    public project[] getProjects() {
        return projects;
    }

    public provider[] getProvider() {
        return Provider;
    }

    public int getTotalProvidersNumber() {
        return TotalProvidersNumber;
    }

    public double getAvPenalty() {
        return AvPenalty;
    }

    public double getAvItems() {
        return AvItems;
    }
}