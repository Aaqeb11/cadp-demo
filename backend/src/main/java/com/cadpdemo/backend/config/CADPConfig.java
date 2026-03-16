@Configuration
public class CADPConfig {

    @Value("${cadp.server}")
    private String server;

    @Value("${cadp.registration-token}")
    private String regToken;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CommandLineRunner registerCADPClient() {
        return args -> {
            RegisterClientParameters params =
                new RegisterClientParameters.Builder(
                    server,
                    regToken.toCharArray()
                ).build();
            new CentralManagementProvider(params).addProvider();
            System.out.println("CADP client registered.");
        };
    }
}
