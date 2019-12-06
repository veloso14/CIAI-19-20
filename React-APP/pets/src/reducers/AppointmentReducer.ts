import {Action} from "redux"
import {AppointmentActionsTypes, ReceiveAppointmentsAction} from '../actions/ScheduleAction';
import {AppointmentState} from "../components/Schedule";
import {UpdateAppointmentAction} from "../actions/AppointmentActions";


const initialState = {
    appointments: [],
    apt: {
        id: -1,
        date: -1,
        desc: "",
        vetID: "",
        petID: ""
    },
    isFetching: false,
};

function appointmentReducer(state: AppointmentState = initialState, action: Action): AppointmentState {
    switch (action.type) {

        case AppointmentActionsTypes.RECEIVE_APPOINTMENTS:
            return {...state, isFetching: false, appointments: (action as ReceiveAppointmentsAction).data};

        case AppointmentActionsTypes.REQUEST_APPOINTMENTS:
            return {...state, isFetching: true};

        case AppointmentActionsTypes.UPDATE_APPOINTMENT:
            return {
                ...state,
                apt: (action as UpdateAppointmentAction).data.apt
            };

        default:
            return state
    }
}

export default appointmentReducer