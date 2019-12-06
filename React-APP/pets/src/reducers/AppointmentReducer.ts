import {Action} from "redux"
import {AppointmentActionsTypes, ReceiveAppointmentsAction} from '../actions/ScheduleAction';
import {AppointmentState} from "../components/Schedule";


const initialState = {
    appointments: [],
    isFetching: false,
};

function appointmentReducer(state: AppointmentState = initialState, action: Action): AppointmentState {
    switch (action.type) {

        case AppointmentActionsTypes.RECEIVE_APPOINTMENTS:
            return {...state, isFetching: false, appointments: (action as ReceiveAppointmentsAction).data};

        case AppointmentActionsTypes.REQUEST_APPOINTMENTS:
            return {...state, isFetching: true};

        default:
            return state
    }
}

export default appointmentReducer