import React from 'react';
import useForm from "react-hook-form";
import {connect} from "react-redux";
import {GlobalState} from "../App";
// @ts-ignore
import Calendar from 'react-calendar';
import {Link} from "react-router-dom"
import Container from "react-bootstrap/Container";
import ListGroup from "react-bootstrap/ListGroup";
import Button from "react-bootstrap/Button";
import {fetchVetAppointments} from "../actions/ScheduleAction";


export interface Appointment {
    id: number,
    date: number,
    desc: string,
}

export interface AppointmentState {
    appointments: Appointment[],
    isFetching: boolean
}

type FormData = {
    petName: string;
    petSpecies: string;
}


const clientID = "1"


const ProtoAppointmentList = (props: { appointments: Appointment[], isFetching: boolean, loadAppointment: (id:string)
        => void, postAppointment: (appointment: Appointment) => void, deleteAppointment: (id: number) => void }) => {
    const [update, setUpdate] = React.useState(false);
    const {register, setValue, handleSubmit, errors} = useForm<FormData>();
    const onSubmit = handleSubmit(({petName, petSpecies}) => {
        console.log(petName, petSpecies);
      //  props.postAppointment({name: petName, species: petSpecies, id: 0, ownerID: clientID});
        setUpdate(true);
    });

    // eslint-disable-next-line
    React.useEffect(() => {
        console.log("run effect");
        props.loadAppointment(clientID);

    }, []);

    let list = props.appointments.map((appointment: Appointment) => {
        return (
            <ListGroup.Item key={appointment.id}>
            <Link to={`/vets/${appointment.id}/appointments/`}>Link to appointment</Link>
        <Button className="float-right" variant="primary" size="sm" onClick={() => {
            props.deleteAppointment(appointment.id);
            setUpdate(true)
        }}>Delete</Button>
        </ListGroup.Item>
    )
    });

    let emptyList = (<p className="text-center">You currently don't have any pets registered!</p>)

    let petList = ( (props.appointments.length > 0) ? <ListGroup>{list}</ListGroup> : emptyList)



    return (
        <Container>
            <br/>
        <h1 className="text-center">My Pets</h1>
    <br/>

    {props.isFetching ? <p>Loading...</p> : petList}

        <br/>
        <h1 className="text-center">Appointments</h1>
            <div>
                <Calendar
                />
            </div>

        </Container>
);
};

const mapStateToProps = (state: GlobalState) => ({
    pets: state.pets.pets,

    appointments: state.appointments.appointments
});

const mapDispatchToProps = (dispatch: any) => {
    return {
     /*   postAppointment: (appointment: Appointment) => {
            dispatch(fetchAppointment())
        },*/
        loadAppointment: (id:string) => {
            dispatch(fetchVetAppointments(id))
        },
    }
};
export default connect(mapStateToProps, mapDispatchToProps)(ProtoAppointmentList);