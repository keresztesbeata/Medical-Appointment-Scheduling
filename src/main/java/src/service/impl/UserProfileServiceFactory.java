package src.service.impl;

import src.model.users.AccountType;
import src.service.api.UserProfileService;

public class UserProfileServiceFactory {
    public static UserProfileService getUserProfileService(AccountType accountType) {
        return switch (accountType) {
            case PATIENT -> new PatientServiceImpl();
            case DOCTOR -> new DoctorServiceImpl();
            case RECEPTIONIST -> new ReceptionistServiceImpl();
        };
    }
}
