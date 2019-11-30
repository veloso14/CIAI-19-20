import React from 'react';
import useForm from "react-hook-form";
import {connect} from "react-redux";
import {deletePetRequest, fetchPets, postPet} from "../actions/PetActions";
import {GlobalState} from "../App";
import {Link} from "react-router-dom"

/*
* #TODO: pagina individual para um pet
*        browserRouter com switch e Links
*        updatePet reducer/action/button
* */

export interface Pet {
    id: number,
    name: string,
    species: string
}

export interface PetState {
    pets: Pet[],
    isFetching: boolean
}

type FormData = {
    petName: string;
    petSpecies: string;
}

const ProtoPetList = (props: { pets: Pet[], isFetching: boolean, loadPets: () => void, postPet: (pet: Pet) => void, deletePet: (id: number) => void }) => {
    const [update, setUpdate] = React.useState(false)
    // eslint-disable-next-line
    const {register, setValue, handleSubmit, errors} = useForm<FormData>();
    const onSubmit = handleSubmit(({petName, petSpecies}) => {
        console.log(petName, petSpecies);
        props.postPet({name: petName, species: petSpecies, id: 0});
        setUpdate(true)
        setValue("petName", "");
        setValue("petSpecies", "");
    });

    // eslint-disable-next-line
    React.useEffect(() => {
        console.log("run effect");
        props.loadPets()
        return () => {
            setUpdate(false)
        }
    }, [update]);


    let list = props.pets.map((pet: Pet) => <li key={pet.id}><Link to={`/pet/${pet.id}`}>{pet.name}</Link>
        <div className='deleteMe' onClick={() => {
            props.deletePet(pet.id);
            setUpdate(true)
        }}>X
        </div>
    </li>);

    return (
        <>
            <h1>Pet List Component</h1>
            <ul>{list}</ul>
            <form onSubmit={onSubmit}>
                <label>Pet Name</label>
                <input name="petName" ref={register}/>
                <label>Pet Species</label>
                <input name="petSpecies" ref={register}/>
                <input type="submit"/>
            </form>
        </>
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
        loadPets: () => {
            dispatch(fetchPets())
        },
        postPet: (pet: Pet) => {
            dispatch(postPet(pet))
        },
        deletePet: (id: number) => {
            dispatch(deletePetRequest(id))
        }
    }
};
export const PetList = connect(mapStateToProps, mapDispatchToProps)(ProtoPetList);