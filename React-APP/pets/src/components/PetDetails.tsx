import React from 'react';
import {Pet} from "./PetList";
import {GlobalState} from "../App";
import {updatePetRequest} from "../actions/PetActions";
import {connect} from "react-redux";
import {useParams} from "react-router-dom"

function fetchPet(id: string) {

    return fetch(`/pets/${id}`, {
        method: "GET",
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                console.log(`Error: ${response.status}: ${response.statusText}`);
                console.error(response)
            }
        })
        .catch(reason => {
            console.log(reason);
        });
}

const PetDetails = (props: { loadPet: (id: string) => void }) => {
    let {id} = useParams();
    // @ts-ignore


    React.useEffect(() => {
        console.log("run effect ");
        // @ts-ignore
        //props.loadPet(id)
        const {pet} = fetchPet(id.toString())
        console.log(JSON.stringify(pet))
    }, []);
    return (
        <div>
            <h1>Details</h1>
        </div>
    );
};


const mapStateToProps = (state: GlobalState) => {
    return {
        pets: state.pets.pets,
        isFetching: state.pets.isFetching
    }
};
const mapDispatchToProps = (dispatch: any) => {
    return {
        /*loadPet: (id: string) => {
            dispatch(fetchPet(id))
        },*/
        updatePet: (id: number, newPet: Pet) => {
            dispatch(updatePetRequest(id, newPet))
        },
    }
};
export default connect(mapStateToProps, mapDispatchToProps)(PetDetails);