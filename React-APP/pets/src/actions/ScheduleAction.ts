import {getData} from "../Utils/NetworkUtils";
import {Action} from "redux";
import {Appointment} from "../components/PetList";


export enum AppointmentActionsTypes {
    GET_APPOINTMENT = 'UPDATE_APPOINTMENT'
}

export interface AddAppointmentAction extends Action {
    name: string,
    species: string
}

export interface ReceiveAppointmentAction extends Action {
    data: Appointment[]
}


export const receiveAppointment = (data: {}) => ({type: AppointmentActionsTypes.GET_APPOINTMENT, data: data});

export function fetchAppointment(id: number) {
    return (dispatch: any) => {
        return getData(`/vets/appointments/${+id}`, {})
            .then(data => {
                console.log("appointment fetch log: " + JSON.stringify(data))
                data && dispatch(receiveAppointment(data))
            })
        // notice that there is an extra "pet" in the path above which is produced
        // in this particular implementation of the service. {pet: Pet, appointments:List<AppointmentDTO>}
    }
}


