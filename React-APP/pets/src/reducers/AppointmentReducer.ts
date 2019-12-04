import {Action} from "redux"
import {
    AppointmentActionsTypes, ReceiveAppointmentAction
} from '../actions/ScheduleAction';
import {AppointmentState} from "../components/Schedule";


const initialState = {
    appointments: [],
    isFetching: false,
};

function appointmentReducer(state: AppointmentState = initialState, action: Action): AppointmentState {
    switch (action.type) {

        case AppointmentActionsTypes.GET_APPOINTMENT:
            return {...state, isFetching: false, appointments: (action as ReceiveAppointmentAction).data};

        default:
            return state
    }
}

export default appointmentReducer