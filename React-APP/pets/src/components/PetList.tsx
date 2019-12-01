import React from 'react';
import useForm from "react-hook-form";
import {connect} from "react-redux";
import {deletePetRequest, fetchPets, postPet} from "../actions/PetActions";
import {GlobalState} from "../App";
import {Link} from "react-router-dom"
import Container from "react-bootstrap/Container";
import ListGroup from "react-bootstrap/ListGroup";
import Button from "react-bootstrap/Button";

export interface Pet {
    id: number,
    name: string,
    species: string
}

export interface Appointment {
    id: number,
    date: number,
    desc: string,
}

export interface PetState {
    pets: Pet[],
    pet: Pet,
    appointments: Appointment[],
    isFetching: boolean
}

type FormData = {
    petName: string;
    petSpecies: string;
}

const ProtoPetList = (props: { pets: Pet[], isFetching: boolean, loadPets: () => void, postPet: (pet: Pet) => void, deletePet: (id: number) => void }) => {
    const [update, setUpdate] = React.useState(false);
    const {register, setValue, handleSubmit, errors} = useForm<FormData>();
    const onSubmit = handleSubmit(({petName, petSpecies}) => {
        console.log(petName, petSpecies);
        props.postPet({name: petName, species: petSpecies, id: 0});
        setUpdate(true);
        setValue("petName", "");
        setValue("petSpecies", "");
    });

    // eslint-disable-next-line
    React.useEffect(() => {
        console.log("run effect");
        props.loadPets();
        return () => {
            setUpdate(false)
        }
    }, [update]);

    let list = props.pets.map((pet: Pet) => {
        return (
            <ListGroup.Item key={pet.id}>
                <Link to={`/pet/${pet.id}`}>{pet.name}</Link>
                <Button className="float-right" variant="primary" size="sm" onClick={() => {
                    props.deletePet(pet.id);
                    setUpdate(true)
                }}>Delete</Button>
            </ListGroup.Item>
        )
    });

    return (
        <Container>
            <br/>
            <h1 className="text-center">My Pets</h1>
            <br/>

            {props.isFetching ? <p>Loading...</p> : <ListGroup>{list}</ListGroup>}

            <br/>
            <h1 className="text-center">Add new pet</h1>

            <form onSubmit={onSubmit}>
                <div className="form-group">
                    <label>Pet Name</label>
                    <input className="form-control" id="petName" name="petName" ref={register({required: true})}/>
                    {errors.petName && 'Pet name is required'}
                </div>
                <div className="form-group">
                    <label>Pet Species</label>
                    <select className="form-control" id="petSpecies" name="petSpecies" ref={register}>
                        <option value="cat">Cat</option>
                        <option value="dog">Dog</option>
                        <option value="bird">Bird</option>
                    </select>
                </div>
                <input className="btn btn-primary float-right" type="submit" value="Add Pet"/>
            </form>
        </Container>
    );
};

const mapStateToProps = (state: GlobalState) => ({
    pets: state.pets.pets,
    isFetching: state.pets.isFetching
});

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
export default connect(mapStateToProps, mapDispatchToProps)(ProtoPetList);