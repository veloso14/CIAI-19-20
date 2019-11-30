import {Action} from "redux"
import {
    PetActionsTypes,
    AddPetAction,
    DeletePetAction,
    ReceivePetAction,
} from '../actions/PetActions';
import {PetState} from "../components/PetList";

const initialState = {
    pets: [],
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
        case PetActionsTypes.REQUEST_PETS:
            return {...state, isFetching: true};
        case PetActionsTypes.RECEIVE_PETS:
            return {...state, isFetching: false, pets: (action as ReceivePetAction).data};
        case PetActionsTypes.REQUEST_PET:
            return {...state, isFetching: true};
        case PetActionsTypes.RECEIVE_PET:
            return {...state, isFetching: false, pets: (action as ReceivePetAction).data};
        case PetActionsTypes.DELETE_PET:
            return {...state, pets: state.pets.filter(pet => pet.id !== (action as DeletePetAction).id)};
        default:
            return state
    }
}

export default petReducer