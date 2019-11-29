import {Action} from "redux"
import {
    ADD_PET,
    AddPetAction,
    DELETE_PET,
    DeletePetAction,
    RECEIVE_PETS,
    ReceivePetAction,
    REQUEST_PETS
} from '../actions/PetActions';
import {PetState} from "../components/PetList";

const initialState = {
    pets: [],
    isFetching: false,
};

function petReducer(state: PetState = initialState, action: Action): PetState {
    switch (action.type) {
        case ADD_PET:
            return {...state,
                pets: [...state.pets, {
                    id: 0,
                    name: (action as AddPetAction).name,
                    species: (action as AddPetAction).species,
                    ownerID: (action as AddPetAction).ownerID
                }]
            };
        case REQUEST_PETS:
            return {...state, isFetching: true};
        case RECEIVE_PETS:
            return {...state, isFetching: false, pets: (action as ReceivePetAction).data};
        case DELETE_PET:
            return {...state, pets: state.pets.filter(pet => pet.id !== (action as DeletePetAction).id)};
        default:
            return state
    }
}

export default petReducer