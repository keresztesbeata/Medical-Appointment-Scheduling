import React, {Component} from 'react';
import {BrowserRouter, Route, Routes} from 'react-router-dom'
import './App.css';
import Login from "./pages/Login";
import Home from "./pages/Home";
import Register from "./pages/Register";
import Logout from "./pages/Logout";
import Header from "./components/Header";
import Error from "./pages/Error";
import ViewPatientProfile from "./pages/ViewPatientProfile";
import ViewDoctorProfile from "./pages/ViewDoctorProfile";
import SetupPatientProfile from "./pages/SetupPatientProfile";
import SetupDoctorProfile from "./pages/SetupDoctorProfile";
import CreateAppointment from "./pages/CreateAppointment";
import PatientViewAppointments from "./pages/PatientViewAppointments";
import ViewAllDoctors from "./pages/ViewAllDoctors";
import PatientViewPrescriptions from "./pages/PatientViewPrescriptions";
import ReceptionistManageAppointments from "./pages/ReceptionistManageAppointments";
import ReceptionistScheduleAppointments from "./pages/ReceptionistScheduleAppointments";
import DoctorViewAppointments from "./pages/DoctorViewAppointments";
import AddPrescription from "./pages/AddPrescription";

class App extends Component {
    render() {
        return (
            <>
                <Header/>
                <BrowserRouter>
                    <Routes>
                        <Route path="/" element={<Home/>}/>
                        <Route path="/login" element={<Login/>}/>
                        <Route path="/register" element={<Register/>}/>
                        <Route path="/logout" element={<Logout/>}/>
                        <Route path="/patient/view_profile" element={<ViewPatientProfile/>}/>
                        <Route path="/patient/setup_profile" element={<SetupPatientProfile/>}/>
                        <Route path="/patient/new_appointment" element={<CreateAppointment/>}/>
                        <Route path="/patient/view_appointments" element={<PatientViewAppointments/>}/>
                        <Route path="/patient/view_prescriptions" element={<PatientViewPrescriptions/>}/>
                        <Route path="/patient/view_doctors" element={<ViewAllDoctors/>}/>
                        <Route path="/doctor/view_profile" element={<ViewDoctorProfile/>}/>
                        <Route path="/doctor/setup_profile" element={<SetupDoctorProfile/>}/>
                        <Route path="/doctor/view_appointments" element={<DoctorViewAppointments/>}/>
                        <Route path="/doctor/add_prescription" element={<AddPrescription/>}/>
                        <Route path="/receptionist/manage_appointments" element={<ReceptionistManageAppointments/>}/>
                        <Route path="/receptionist/schedule_appointments" element={<ReceptionistScheduleAppointments/>}/>
                        <Route path="/receptionist/view_doctors" element={<ViewAllDoctors/>}/>
                        <Route path="/error" element={<Error/>}/>
                    </Routes>
                </BrowserRouter>
            </>
        );
    }
}

export default App;
