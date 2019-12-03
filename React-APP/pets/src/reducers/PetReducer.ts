import {Action} from "redux"
import {
    AddPetAction,
    DeletePetAction,
    PetActionsTypes,
    ReceivePetAction,
    ReceivePetsAction,
    UpdatePetAction,
} from '../actions/PetActions';
import {PetState} from "../components/PetList";

const initialState = {
    pets: [],
    pet: {
        id: -1,
        name: "",
        species: "",
    },
    appointments: [],
    isFetching: false,
};

function petReducer(state: PetState = initialState, action: Action): PetState {
    switch (action.type) {
        case PetActionsTypes.ADD_PET:
            return {
                ...state,
                pets: [...state.pets, {
                    id: 0,
                    name: (action as AddPetAction).name,
                    species: (action as AddPetAction).species,
                }]
            };
        case PetActionsTypes.UPDATE_PET:
            return {
                ...state,
                pet: (action as UpdatePetAction).data.pet
            };

        case PetActionsTypes.REQUEST_PETS:
            return {...state, isFetching: true};
        case PetActionsTypes.RECEIVE_PETS:
            return {...state, isFetching: false, pets: (action as ReceivePetsAction).data};


        case PetActionsTypes.REQUEST_PET:
            return {...state, isFetching: true};
        case PetActionsTypes.RECEIVE_PET:
            return {
                ...state,
                isFetching: false,
                pet: (action as ReceivePetAction).data.pet,
                appointments: (action as ReceivePetAction).data.appointments
            };


        case PetActionsTypes.DELETE_PET:
            return {...state, pets: state.pets.filter(pet => pet.id !== (action as DeletePetAction).id)};
        default:
            return state
    }
}

export default petReducer