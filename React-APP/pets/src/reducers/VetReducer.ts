import {Action} from "redux"
import {
    AddVetAction,
    DeleteVetAction,

    VetActionsTypes,
    ReceiveVetAction,
    ReceiveVetsAction,
    UpdateVetAction,
} from '../actions/VetActions';
import {VetState} from "../components/VetList";


const initialState = {
    vets: [],
    vet: {
        id: -1,
        name: "",
        employeeID: -1,
        photo: "",
        address: "",
        password: "",
        username: "",
        cellphone: -1,
        email: ""
    },
    appointments: [],
    schedules: [],
    isFetching: false,
};

function vetReducer(state: VetState = initialState, action: Action): VetState {
    switch (action.type) {
        case VetActionsTypes.ADD_VET:
            return {
                ...state,
                vets: [...state.vets, {
                    id: 0,
                    name: (action as AddVetAction).name,
                    employeeID: (action as AddVetAction).employeeID,
                    photo:  (action as AddVetAction).photo,
                    address:  (action as AddVetAction).address,
                    password: (action as AddVetAction).password,
                    username:  (action as AddVetAction).username,
                    cellphone:  (action as AddVetAction).cellphone,
                    email:  (action as AddVetAction).email,
                }]
            };
        case VetActionsTypes.UPDATE_VET:
            return {
                ...state,
                vet: (action as UpdateVetAction).data.vet
            };

        case VetActionsTypes.REQUEST_VETS:
            return {...state, isFetching: true};
        case VetActionsTypes.RECEIVE_VETS:
            return {...state, isFetching: false, vets: (action as ReceiveVetsAction).data};


        case VetActionsTypes.REQUEST_VET:
            return {...state, isFetching: true};
        case VetActionsTypes.RECEIVE_VET:
            return {
                ...state,
                isFetching: false,
                vet: (action as ReceiveVetAction).data.vet,
                appointments: (action as ReceiveVetAction).data.appointments,
                schedules: (action as ReceiveVetAction).data.schedules
            };


       // case VetActionsTypes.DELETE_PET:
         //   return {...state, pets: state.pets.filter(pet => pet.id !== (action as DeletePetAction).id)};
        default:
            return state
    }
}

export default vetReducer